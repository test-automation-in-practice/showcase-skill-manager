package skillmanagement.test.docker

import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource
import org.springframework.core.io.Resource
import org.testcontainers.containers.GenericContainer
import java.io.BufferedReader

class PostgresContainer : Container("postgres:9.6") {
    override fun getMappedPort(): Int = getMappedPort(5432)
}

abstract class Container(image: String) : GenericContainer<Container>(image), CloseableResource {

    fun addEnvFromFile(resource: Resource) {
        resource.readAsString().lines()
            .filter { it.isNotBlank() }
            .map { line -> line.split('=').map(String::trim) }
            .filter { it.size == 2 }
            .forEach { (key, value) -> addEnv(key, value) }
    }

    abstract fun getMappedPort(): Int

    private fun Resource.readAsString(): String =
        inputStream.bufferedReader().use(BufferedReader::readText)

}
