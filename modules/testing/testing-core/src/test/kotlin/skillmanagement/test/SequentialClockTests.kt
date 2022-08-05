package skillmanagement.test

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@UnitTest
internal class SequentialClockTests {

    @Test
    fun `empty clock will throw exception at first use`() {
        val clock = sequentialClock()
        assertThrows<NoSuchElementException> { clock.instant() }
    }

    @Test
    fun `clock will return instant instances as long as there are some defined`() {
        val clock = sequentialClock(
            "2019-11-23T12:34:56.001Z",
            "2019-11-23T12:34:56.002Z",
            "2019-11-23T12:34:56.003Z"
        )
        Assertions.assertThat(clock.instant()).isEqualTo("2019-11-23T12:34:56.001Z")
        Assertions.assertThat(clock.instant()).isEqualTo("2019-11-23T12:34:56.002Z")
        Assertions.assertThat(clock.instant()).isEqualTo("2019-11-23T12:34:56.003Z")
        assertThrows<NoSuchElementException> { clock.instant() }
    }

}
