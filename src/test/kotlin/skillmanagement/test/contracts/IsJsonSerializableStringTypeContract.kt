package skillmanagement.test.contracts

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotlintest.shouldBe
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

private val defaultObjectMapper = jacksonObjectMapper()

interface IsJsonSerializableStringTypeContract : StringTypeContract {

    @TestFactory
    fun `is serialized to JSON string value`() = validExampleValues
        .map { example ->
            dynamicTest(example) {
                val instance = createInstance(example)
                val expectedJsonValue = '"' + example + '"'
                defaultObjectMapper.writeValueAsString(instance) shouldBe expectedJsonValue
            }
        }

    @TestFactory
    fun `can be de-serialized from JSON string value`() = validExampleValues
        .map { example ->
            dynamicTest(example) {
                val jsonValue = '"' + example + '"'
                val expectedInstance = createInstance(example)
                defaultObjectMapper.readValue(jsonValue, expectedInstance::class.java) shouldBe expectedInstance
            }
        }

}
