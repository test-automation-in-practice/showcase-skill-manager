package skillmanagement.domain.projects.usecases.read

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.domain.projects.model.project_representation_neo
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class GetProjectByIdGraphQLAdapterTests {

    private val getProjectById: GetProjectByIdFunction = mockk()
    private val cut = GetProjectByIdGraphQLAdapter(getProjectById)

    @Test
    fun `translates and delegates retrieval to business function - found`() {
        every { getProjectById(project_neo.id) } returns project_neo
        assertThat(tryToGetProject()).isEqualTo(project_representation_neo)
    }

    @Test
    fun `translates and delegates retrieval to business function - not found`() {
        every { getProjectById(project_neo.id) } returns null
        assertThat(tryToGetProject()).isNull()
    }

    private fun tryToGetProject(id: String = "f804d83f-466c-4eab-a58f-4b25ca1778f3") = cut.getProjectById(id)

}
