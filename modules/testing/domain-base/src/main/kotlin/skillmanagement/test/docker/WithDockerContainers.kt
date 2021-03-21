package skillmanagement.test.docker

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Namespace
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource
import java.util.concurrent.CompletableFuture.supplyAsync
import java.util.concurrent.TimeUnit.MINUTES
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation

@Retention
@Target(CLASS)
@ExtendWith(WithDockerContainerExtension::class)
annotation class WithDockerContainers(
    vararg val value: KClass<out DockerContainerFactory<*>>
)

private class WithDockerContainerExtension : BeforeAllCallback {

    /*
        This is not a perfect implementation! It will not work for all possible JUnit 5 test constructs.
        Especially @Nested classes with different Container needs might not work. But since something like
        that is currently not needed, this extension is good enough.

        The main reason for writing a custom extension instead of using TestContainer's official JUnit 5
        extension is that I don't want to have to declare containers for each test class in companion objects.

        Instead I'd rather have this bit of code that enables everything the current tests actually need.
     */

    private val namespace: Namespace = Namespace.create(javaClass)

    override fun beforeAll(context: ExtensionContext) {
        if (context.containers.isEmpty()) {
            getContainerFactoryClasses(context)
                .map { factoryClass -> factoryClass.createInstance() }
                .map { factory ->
                    supplyAsync {
                        factory.createContainer()
                            .apply {
                                addExposedPort(port)
                                start()
                            }
                    }
                }
                .map { supplier -> supplier.get(1, MINUTES) }
                .forEach { container ->
                    context.containers.add(container)
                    System.setProperty(container.portProperty, "${container.getMappedPort()}")
                }
        }
    }

    private fun getContainerFactoryClasses(context: ExtensionContext): List<KClass<out DockerContainerFactory<*>>> {
        val annotations = getConfigurationAnnotations(context)
        return annotations.flatMap { annotation -> annotation.value.toList() }.distinct()
    }

    private fun getConfigurationAnnotations(context: ExtensionContext): List<WithDockerContainers> {
        val testClass = context.requiredTestClass

        val directAnnotations = testClass.getAnnotationsByType(WithDockerContainers::class.java).toList()
        val viaFirstLevelComposites = testClass.annotations.mapNotNull {
            it.annotationClass.findAnnotation<WithDockerContainers>()
        }
        val annotations = directAnnotations + viaFirstLevelComposites
        check(annotations.isNotEmpty()) { "could not find ${WithDockerContainers::class} annotation :(" }
        return annotations
    }

    private val ExtensionContext.containers: Containers
        get() = getStore(namespace).getOrComputeIfAbsent("containers", { Containers() }, Containers::class.java)

    private class Containers : CloseableResource {
        private val delegate = mutableListOf<Container>()
        fun add(container: Container) = delegate.add(container)
        fun isEmpty() = delegate.isEmpty()
        override fun close() = delegate.forEach(Container::close)
    }

}
