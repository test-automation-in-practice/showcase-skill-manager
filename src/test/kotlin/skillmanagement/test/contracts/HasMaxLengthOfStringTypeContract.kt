package skillmanagement.test.contracts

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

interface HasMaxLengthOfStringTypeContract : StringTypeContract {

    val maxLength: Int

    @Test
    fun `instances with max-length values can be initialized`() {
        createInstanceOfLength(maxLength)
    }

    @Test
    fun `instances with greater than max-length values cannot be initialized`() {
        assertThatThrownBy { createInstanceOfLength(maxLength + 1) }
            .hasMessageContaining("must not be longer than $maxLength characters, but is ${maxLength + 1} characters long!")
    }

    fun createInstanceOfLength(length: Int): Any

}
