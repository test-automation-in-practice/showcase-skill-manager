package skillmanagement.test.contracts

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

interface IsNotBlankStringTypeContract : StringTypeContract {

    @Test
    fun `instances with non-blank values can be initialized`() {
        createInstance(validExampleValues.first())
    }

    @TestFactory
    fun `instances with blank value cannot be initialized`() =
        listOf(
            "empty string" to "",
            "single space" to " ",
            "multiple spaces" to "   ",
            "single tab" to "\t",
            "multiple tabs" to "\t\t\t",
            "single line break" to "\n",
            "multiple line breaks" to "\n\n\n",
            "mixed" to "\n \t"
        ).map { (name, example) ->
            dynamicTest(name) {
                assertThatThrownBy { createInstance(example) }
                    .hasMessageContaining("must not be blank!")
            }
        }

}
