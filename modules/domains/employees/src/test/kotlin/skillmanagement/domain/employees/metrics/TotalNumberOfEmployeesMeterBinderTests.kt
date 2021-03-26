package skillmanagement.domain.employees.metrics

import io.kotlintest.shouldBe
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class TotalNumberOfEmployeesMeterBinderTests {

    private val getTotalNumberOfEmployeesFromDataStore: GetTotalNumberOfEmployeesFromDataStoreFunction = mockk()
    private val registry = SimpleMeterRegistry()
    private val cut = TotalNumberOfEmployeesMeterBinder(getTotalNumberOfEmployeesFromDataStore)
        .apply { bindTo(registry) }

    @Test
    fun `gauge is instantiated with a value of 0`() {
        getCurrentGaugeValue() shouldBe 0
    }

    @Test
    fun `gauge is updated via an external trigger`() {
        getCurrentGaugeValue() shouldBe 0
        stubNumberOfEmployeesInDataStore(42)
        getCurrentGaugeValue() shouldBe 0
        cut.updateCache()
        getCurrentGaugeValue() shouldBe 42
    }

    @Test
    fun `gauge value can go up and down`() {
        stubNumberOfEmployeesInDataStore(42)
        cut.updateCache()
        getCurrentGaugeValue() shouldBe 42

        stubNumberOfEmployeesInDataStore(21)
        cut.updateCache()
        getCurrentGaugeValue() shouldBe 21
    }

    private fun getCurrentGaugeValue(): Long? =
        registry.find("employees.total").gauge()?.value()?.toLong()

    private fun stubNumberOfEmployeesInDataStore(number: Long) {
        every { getTotalNumberOfEmployeesFromDataStore() } returns number
    }

}
