package skillmanagement.test.contracts

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import skillmanagement.common.validation.ValidationException

interface MatchesPatternStringTypeContract : StringTypeContract {

    val invalidExampleValues: Iterable<String>

    @TestFactory
    fun `examples of invalid values`(): List<DynamicTest> = invalidExampleValues.map { value ->
        DynamicTest.dynamicTest(value) { assertThrows<ValidationException> { createInstance(value) } }
    }

}
