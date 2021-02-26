package skillmanagement.test.contracts.string

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

private val defaultObjectMapper = jacksonObjectMapper()

interface IsJsonSerializableContract : StringTypeContract {

    @TestFactory
    fun `is serialized to JSON string value`() = validExamples
        .map { example ->
            dynamicTest(example) {
                val instance = createInstance(example)
                val expectedJsonValue = defaultObjectMapper.writeValueAsString(example)
                assertThat(defaultObjectMapper.writeValueAsString(instance)).isEqualTo(expectedJsonValue)
            }
        }

    @TestFactory
    fun `can be de-serialized from JSON string value`() = validExamples
        .map { example ->
            dynamicTest(example) {
                val jsonValue = defaultObjectMapper.writeValueAsString(example)
                val expectedInstance = createInstance(example)
                assertThat(defaultObjectMapper.readValue(jsonValue, expectedInstance::class.java))
                    .isEqualTo(expectedInstance)
            }
        }

}
