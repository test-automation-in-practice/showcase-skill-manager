package skillmanagement.test.docker

import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import kotlin.annotation.AnnotationTarget.CLASS

@Retention
@Target(CLASS)
@ExtendWith(RabbitMqContainerExtension::class)
annotation class RunWithDockerizedRabbitMq

private class RabbitMqContainerExtension : AbstractDockerContainerExtension<RabbitMqContainer>() {

    override val port = 5672
    override val portProperty = "RABBITMQ_PORT"
    override fun createResource() = RabbitMqContainer()

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean =
        parameterContext.parameter.type == RabbitMqContainer::class.java

}
