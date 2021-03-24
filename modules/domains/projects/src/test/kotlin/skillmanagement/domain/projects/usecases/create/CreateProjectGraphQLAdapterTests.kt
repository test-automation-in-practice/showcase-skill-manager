package skillmanagement.domain.projects.usecases.create

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.domain.projects.model.project_creation_data_neo
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class CreateProjectGraphQLAdapterTests {

    private val createProject: CreateProjectFunction = mockk()
    private val cut = CreateProjectGraphQLAdapter(createProject)

    @Test
    fun `delegates creation to business function`() {
        every { createProject(project_creation_data_neo) } returns project_neo
        assertThat(cut.createProject(project_creation_data_neo)).isEqualTo(project_neo)
    }

}
