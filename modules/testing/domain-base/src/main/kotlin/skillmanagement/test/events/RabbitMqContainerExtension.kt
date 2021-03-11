package skillmanagement.test.events

import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import skillmanagement.test.docker.AbstractDockerContainerExtension
import skillmanagement.test.docker.Container
import kotlin.annotation.AnnotationTarget.CLASS

@Retention
@Target(CLASS)
@ExtendWith(RabbitMqContainerExtension::class)
annotation class RunWithDockerizedRabbitMq

private class RabbitMqContainer : Container("rabbitmq:3.8") {
    override fun getMappedPort(): Int = getMappedPort(5672)
}

private class RabbitMqContainerExtension : AbstractDockerContainerExtension<RabbitMqContainer>() {

    override val port = 5672
    override val portProperty = "RABBITMQ_PORT"
    override fun createResource() = RabbitMqContainer()

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean =
        parameterContext.parameter.type == RabbitMqContainer::class.java

}
