package skillmanagement.test

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging.logger
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import kotlin.reflect.KClass

@JsonTest
abstract class AbstractJsonDeserializationTests<T : Any> {

    private val log = logger {}

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    abstract val deserializationExamples: List<Pair<String, T>>
    abstract val objectType: KClass<T>

    @TestFactory
    fun `instances can be de-serialized from JSON`() =
        deserializationExamples.mapIndexed { index, (example, expectedInstance) ->
            dynamicTest("Example #${index + 1}") {
                val actualInstance = objectMapper.readValue(example, objectType.java)
                log.info { "Example: $example" }
                log.info { "Actual: $actualInstance" }
                log.info { "Expected: $expectedInstance" }
                assertThat(actualInstance).isEqualTo(expectedInstance)
            }
        }

}
