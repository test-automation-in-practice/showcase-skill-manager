package skillmanagement.test.events

import skillmanagement.test.docker.Container
import skillmanagement.test.docker.DockerContainerFactory

const val RABBITMQ_DOCKER_PORT_PROPERTY = "RABBITMQ_PORT"

class RabbitMqContainer : Container("rabbitmq:3.8") {
    override val port = 5672
    override val portProperty = RABBITMQ_DOCKER_PORT_PROPERTY
}

class RabbitMqContainerFactory : DockerContainerFactory<RabbitMqContainer> {
    override fun createContainer() = RabbitMqContainer()
}
