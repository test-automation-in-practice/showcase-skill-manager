package skillmanagement.test

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.annotation.DirtiesContext
import skillmanagement.test.searchindices.RunWithDockerizedElasticsearch
import skillmanagement.test.docker.RunWithDockerizedPostgres
import skillmanagement.test.events.RunWithDockerizedRabbitMq
import kotlin.annotation.AnnotationTarget.CLASS

const val PROPERTY_DOCKERIZED_DATABASE_URL =
    "storage.database.jdbc-url=jdbc:postgresql://localhost:\${POSTGRES_PORT}/database"
const val PROPERTY_DOCKERIZED_ELASTICSEARCH_PORT = "storage.elasticsearch.port=\${ELASTICSEARCH_PORT}"
const val PROPERTY_DOCKERIZED_BROKER_PORT = "storage.broker.port=\${RABBITMQ_PORT}"

@Retention
@Target(CLASS)
@RunWithDockerizedRabbitMq
@RunWithDockerizedElasticsearch
@RunWithDockerizedPostgres
@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = [PROPERTY_DOCKERIZED_DATABASE_URL, PROPERTY_DOCKERIZED_ELASTICSEARCH_PORT, PROPERTY_DOCKERIZED_BROKER_PORT]
)
@DirtiesContext
@AutoConfigureMockMvc
annotation class SpringBootTestWithDockerizedDependencies
