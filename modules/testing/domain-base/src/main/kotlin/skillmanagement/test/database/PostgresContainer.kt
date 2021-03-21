package skillmanagement.test.database

import org.springframework.core.io.ClassPathResource
import skillmanagement.test.docker.Container
import skillmanagement.test.docker.DockerContainerFactory

const val POSTGRES_DOCKER_PORT_PROPERTY = "POSTGRES_PORT"

class PostgresContainer : Container("postgres:9.6") {
    override val port = 5432
    override val portProperty = POSTGRES_DOCKER_PORT_PROPERTY
}

class PostgresContainerFactory : DockerContainerFactory<PostgresContainer> {
    override fun createContainer() = PostgresContainer()
        .apply { addEnvFromFile(ClassPathResource("postgres.env")) }
}
