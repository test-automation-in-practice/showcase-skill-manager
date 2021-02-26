package skillmanagement.test

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.test.annotation.DirtiesContext
import skillmanagement.test.docker.RunWithDockerizedElasticsearch
import skillmanagement.test.docker.RunWithDockerizedPostgres
import skillmanagement.test.docker.RunWithDockerizedRabbitMq
import kotlin.annotation.AnnotationTarget.CLASS

@Retention
@Target(CLASS)
@RunWithDockerizedRabbitMq
@RunWithDockerizedElasticsearch
@RunWithDockerizedPostgres
@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = [
        "storage.database.jdbc-url=jdbc:postgresql://localhost:\${POSTGRES_PORT}/database",
        "storage.elasticsearch.port=\${ELASTICSEARCH_PORT}",
        "storage.broker.port=\${RABBITMQ_PORT}"
    ]
)
@DirtiesContext
@AutoConfigureMockMvc
annotation class SpringBootTestWithDockerizedDependencies
