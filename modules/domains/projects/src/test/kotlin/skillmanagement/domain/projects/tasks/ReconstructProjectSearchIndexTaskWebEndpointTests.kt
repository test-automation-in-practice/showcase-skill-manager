package skillmanagement.domain.projects.tasks

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class ReconstructProjectSearchIndexTaskWebEndpointTests {

    private val task: ReconstructProjectSearchIndexTask = mockk(relaxUnitFun = true)
    private val cut = ReconstructProjectSearchIndexTaskWebEndpoint(task)

    @Test
    fun `trigger simply runs task`() {
        cut.trigger()
        verify { task.run() }
    }

}
