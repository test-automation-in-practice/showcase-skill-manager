package skillmanagement.domain.projects.usecases.delete

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class DeleteProjectByIdGraphQLAdapterTests {

    private val deleteProjectById: DeleteProjectByIdFunction = mockk()
    private val cut = DeleteProjectByIdGraphQLAdapter(deleteProjectById)

    @Test
    fun `translates and delegates deletion to business function - deleted`() {
        every { deleteProjectById(project_neo.id) } returns true
        assertThat(tryToDeleteSkill()).isTrue()
    }

    @Test
    fun `translates and delegates deletion to business function - not found`() {
        every { deleteProjectById(project_neo.id) } returns false
        assertThat(tryToDeleteSkill()).isFalse()
    }

    private fun tryToDeleteSkill(id: String = "f804d83f-466c-4eab-a58f-4b25ca1778f3") = cut.deleteProjectById(id)
}
