package skillmanagement.test

import skillmanagement.test.Container.MongoDb
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Namespace
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource
import org.testcontainers.containers.GenericContainer
import kotlin.annotation.AnnotationTarget.CLASS

@Retention
@Target(CLASS)
@ExtendWith(MongoDbContainerExtension::class)
annotation class RunWithDockerizedMongoDb

private class MongoDbContainerExtension : AbstractDockerContainerExtension<MongoDb>() {
    override val port: Int = 27017
    override val portProperty: String = "MONGODB_PORT"
    override fun createResource(): MongoDb = MongoDb()
}

private abstract class AbstractDockerContainerExtension<T : Container> : BeforeAllCallback {

    private val namespace = Namespace.create(javaClass)

    protected abstract val port: Int
    protected abstract val portProperty: String

    override fun beforeAll(context: ExtensionContext) {
        if (context.container == null) {
            val resource = createResource()
                .apply { addExposedPort(port) }
                .apply { start() }
            System.setProperty(portProperty, "${resource.getMappedPort(port)}")
            context.container = resource
        }
    }

    protected abstract fun createResource(): T

    private var ExtensionContext.container: T?
        get() = getStore(namespace).get("container") as T?
        set(value) = getStore(namespace).put("container", value)

}

private sealed class Container(image: String) : GenericContainer<Container>(image), CloseableResource {
    class MongoDb : Container("mongo:4.2.3")
}
