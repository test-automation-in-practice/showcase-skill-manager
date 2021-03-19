package skillmanagement.test.docker

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Namespace
import org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import java.util.concurrent.CompletableFuture.supplyAsync

internal abstract class AbstractDockerContainerExtension<T : Container> : BeforeAllCallback, ParameterResolver {

    protected val namespace: Namespace = Namespace.create(javaClass)

    protected abstract val port: Int
    protected abstract val portProperty: String

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any? =
        getOrInitializeContainerResource(extensionContext).getContainer()

    override fun beforeAll(context: ExtensionContext) {
        getOrInitializeContainerResource(context)
    }

    protected fun getOrInitializeContainerResource(context: ExtensionContext): ContainerResource<T> =
        context.container ?: initializeContainerResource(context)

    private fun initializeContainerResource(context: ExtensionContext): ContainerResource<T> {
        val future = supplyAsync { createAndStartContainer() }
        val resource = ContainerResource(future)
        context.container = resource
        context.containers.add(resource)
        return resource
    }

    private fun createAndStartContainer(): T {
        val resource = createResource()
            .apply { addExposedPort(port) }
            .apply { start() }
        System.setProperty(portProperty, "${resource.getMappedPort(port)}")
        return resource
    }

    protected abstract fun createResource(): T

    @Suppress("UNCHECKED_CAST")
    private var ExtensionContext.container: ContainerResource<T>?
        get() = getStore(namespace).get("container") as ContainerResource<T>?
        set(value) = getStore(namespace).put("container", value)

}

@Suppress("UNCHECKED_CAST")
internal val ExtensionContext.containers: MutableSet<ContainerResource<out Container>>
    get() {
        val store = getStore(GLOBAL)
        val containers = store.getOrComputeIfAbsent("containers") {
            mutableSetOf<ContainerResource<out Container>>()
        }
        return containers as MutableSet<ContainerResource<out Container>>
    }
