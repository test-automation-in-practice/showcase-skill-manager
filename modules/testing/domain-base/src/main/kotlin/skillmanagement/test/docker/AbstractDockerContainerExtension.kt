package skillmanagement.test.docker

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Namespace
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver

internal abstract class AbstractDockerContainerExtension<T : Container> : BeforeAllCallback, ParameterResolver {

    protected val namespace: Namespace = Namespace.create(javaClass)

    protected abstract val port: Int
    protected abstract val portProperty: String

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any? =
        getOrInitializeContainer(extensionContext)

    override fun beforeAll(context: ExtensionContext) {
        getOrInitializeContainer(context)
    }

    protected fun getOrInitializeContainer(context: ExtensionContext): T =
        context.container ?: initContainer().also { context.container = it }

    private fun initContainer(): T {
        val resource = createResource()
            .apply { addExposedPort(port) }
            .apply { start() }
        System.setProperty(portProperty, "${resource.getMappedPort(port)}")
        return resource
    }

    protected abstract fun createResource(): T

    @Suppress("UNCHECKED_CAST")
    private var ExtensionContext.container: T?
        get() = getStore(namespace).get("container") as T?
        set(value) = getStore(namespace).put("container", value)

}
