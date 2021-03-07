package skillmanagement.domain.skills.tasks

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class ReconstructSkillSearchIndexTaskWebEndpointTests {

    private val task: ReconstructSkillSearchIndexTask = mockk(relaxUnitFun = true)
    private val cut = ReconstructSkillSearchIndexTaskWebEndpoint(task)

    @Test
    fun `trigger simply runs task`() {
        cut.trigger()
        verify { task.run() }
    }

}
