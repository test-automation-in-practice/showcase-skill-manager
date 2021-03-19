package skillmanagement.test.database

import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.springframework.core.io.ClassPathResource
import skillmanagement.test.docker.AbstractDockerContainerExtension
import skillmanagement.test.docker.Container
import kotlin.annotation.AnnotationTarget.CLASS

@Retention
@Target(CLASS)
@ExtendWith(PostgresContainerExtension::class)
annotation class RunWithDockerizedPostgres

class PostgresContainer : Container("postgres:9.6") {
    override fun getMappedPort(): Int = getMappedPort(5432)
}

private class PostgresContainerExtension : AbstractDockerContainerExtension<PostgresContainer>() {

    override val port = 5432
    override val portProperty = "POSTGRES_PORT"
    override fun createResource() = PostgresContainer()
        .apply { addEnvFromFile(ClassPathResource("postgres.env")) }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean =
        parameterContext.parameter.type == PostgresContainer::class.java

}
