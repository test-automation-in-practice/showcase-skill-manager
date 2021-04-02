package skillmanagement.test

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.NoSuchElementException

@UnitTest
internal class SequentialIdGeneratorTests {

    @Test
    fun `empty ID generator will throw exception at first use`() {
        val generator = sequentialIdGenerator()
        assertThrows<NoSuchElementException> { generator.generateId() }
    }

    @Test
    fun `clock will return instant instances as long as there are some defined`() {
        val generator = sequentialIdGenerator(
            "6b51ac39-55b9-4a13-8b42-c71da567af16",
            "bb75d8c2-de98-46c2-b1a7-c00a32438453",
            "6cdee3f7-8f9a-4b81-a1b8-06a5b2efcd2e"
        )
        assertThat(generator.generateId()).isEqualTo(uuid("6b51ac39-55b9-4a13-8b42-c71da567af16"))
        assertThat(generator.generateId()).isEqualTo(uuid("bb75d8c2-de98-46c2-b1a7-c00a32438453"))
        assertThat(generator.generateId()).isEqualTo(uuid("6cdee3f7-8f9a-4b81-a1b8-06a5b2efcd2e"))
        assertThrows<NoSuchElementException> { generator.generateId() }
    }

}
