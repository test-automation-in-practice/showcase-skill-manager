package skillmanagement.test.e2e

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.annotation.DirtiesContext
import skillmanagement.test.docker.RunWithDockerizedPostgres
import skillmanagement.test.events.RunWithDockerizedRabbitMq
import skillmanagement.test.searchindices.RunWithDockerizedElasticsearch
import kotlin.annotation.AnnotationTarget.CLASS

const val PROPERTY_DOCKERIZED_DATABASE_URL = "spring.datasource.url=" +
        "jdbc:postgresql://localhost:\${POSTGRES_PORT}/database"
const val PROPERTY_DOCKERIZED_DATABASE_USER = "spring.datasource.username=user"
const val PROPERTY_DOCKERIZED_DATABASE_PASSWORD = "spring.datasource.password=password"

const val PROPERTY_DOCKERIZED_ELASTICSEARCH_PORT = "spring.elasticsearch.rest.uris=" +
        "http://localhost:\${ELASTICSEARCH_PORT}"

const val PROPERTY_DOCKERIZED_BROKER_HOST = "spring.rabbitmq.host=localhost"
const val PROPERTY_DOCKERIZED_BROKER_PORT = "spring.rabbitmq.port=\${RABBITMQ_PORT}"

@Retention
@Target(CLASS)
@RunWithDockerizedRabbitMq
@RunWithDockerizedElasticsearch
@RunWithDockerizedPostgres
@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = [
        PROPERTY_DOCKERIZED_DATABASE_URL, PROPERTY_DOCKERIZED_DATABASE_USER, PROPERTY_DOCKERIZED_DATABASE_PASSWORD,
        PROPERTY_DOCKERIZED_ELASTICSEARCH_PORT,
        PROPERTY_DOCKERIZED_BROKER_HOST, PROPERTY_DOCKERIZED_BROKER_PORT
    ]
)
@DirtiesContext
@AutoConfigureMockMvc
annotation class SpringBootTestWithDockerizedDependencies
