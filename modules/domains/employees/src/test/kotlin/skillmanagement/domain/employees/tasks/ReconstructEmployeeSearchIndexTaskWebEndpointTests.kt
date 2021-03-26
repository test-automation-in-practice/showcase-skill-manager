package skillmanagement.domain.employees.tasks

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class ReconstructEmployeeSearchIndexTaskWebEndpointTests {

    private val task: ReconstructEmployeeSearchIndexTask = mockk(relaxUnitFun = true)
    private val cut = ReconstructEmployeeSearchIndexTaskWebEndpoint(task)

    @Test
    fun `trigger simply runs task`() {
        cut.trigger()
        verify { task.run() }
    }

}
