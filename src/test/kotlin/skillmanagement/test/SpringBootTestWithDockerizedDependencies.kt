package skillmanagement.test

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import skillmanagement.test.docker.RunWithDockerizedElasticsearch
import skillmanagement.test.docker.RunWithDockerizedPostgres
import kotlin.annotation.AnnotationTarget.CLASS

@Retention
@Target(CLASS)
@RunWithDockerizedElasticsearch
@RunWithDockerizedPostgres
@SpringBootTest(
    webEnvironment = RANDOM_PORT,
    properties = [
        "storage.database.jdbc-url=jdbc:postgresql://localhost:\${POSTGRES_PORT}/database",
        "storage.elasticsearch.port=\${ELASTICSEARCH_PORT}"
    ]
)
annotation class SpringBootTestWithDockerizedDependencies
