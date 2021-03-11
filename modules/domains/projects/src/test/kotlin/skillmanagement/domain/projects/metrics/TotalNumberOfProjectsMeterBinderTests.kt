package skillmanagement.domain.projects.metrics

import io.kotlintest.shouldBe
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class TotalNumberOfProjectsMeterBinderTests {

    private val getTotalNumberOfProjectsFromDataStore: GetTotalNumberOfProjectsFromDataStoreFunction = mockk()
    private val registry = SimpleMeterRegistry()
    private val cut = TotalNumberOfProjectsMeterBinder(getTotalNumberOfProjectsFromDataStore)
        .apply { bindTo(registry) }

    @Test
    fun `gauge is instantiated with a value of 0`() {
        getCurrentGaugeValue() shouldBe 0
    }

    @Test
    fun `gauge is updated via an external trigger`() {
        getCurrentGaugeValue() shouldBe 0
        stubNumberOfProjectsInDataStore(42)
        getCurrentGaugeValue() shouldBe 0
        cut.updateCache()
        getCurrentGaugeValue() shouldBe 42
    }

    @Test
    fun `gauge value can go up and down`() {
        stubNumberOfProjectsInDataStore(42)
        cut.updateCache()
        getCurrentGaugeValue() shouldBe 42

        stubNumberOfProjectsInDataStore(21)
        cut.updateCache()
        getCurrentGaugeValue() shouldBe 21
    }

    private fun getCurrentGaugeValue(): Long? =
        registry.find("projects.total").gauge()?.value()?.toLong()

    private fun stubNumberOfProjectsInDataStore(number: Long) {
        every { getTotalNumberOfProjectsFromDataStore() } returns number
    }

}
