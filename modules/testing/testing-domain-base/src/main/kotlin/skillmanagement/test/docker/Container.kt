package skillmanagement.test.docker

import org.springframework.core.io.Resource
import org.testcontainers.containers.GenericContainer
import java.io.BufferedReader

abstract class Container(image: String) : GenericContainer<Container>(image) {

    abstract val port: Int
    abstract val portProperty: String

    fun addEnvFromFile(resource: Resource) {
        resource.readAsString().lines()
            .filter { it.isNotBlank() }
            .map { line -> line.split('=').map(String::trim) }
            .filter { it.size == 2 }
            .forEach { (key, value) -> addEnv(key, value) }
    }

    fun getMappedPort(): Int = getMappedPort(port)

    private fun Resource.readAsString(): String =
        inputStream.bufferedReader().use(BufferedReader::readText)

}
