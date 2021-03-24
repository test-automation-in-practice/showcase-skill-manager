package skillmanagement.domain.projects.usecases.read

import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class GetProjectByIdFunctionTests {

    private val getProjectsFromDataStore: GetProjectsFromDataStoreFunction = mockk()
    private val getProjectById = GetProjectByIdFunction(getProjectsFromDataStore)

    @Test
    fun `returns NULL if nothing found with given ID`() {
        every { getProjectsFromDataStore(project_neo.id) } returns null
        getProjectById(project_neo.id) shouldBe null
    }

    @Test
    fun `returns Project if found by its ID`() {
        every { getProjectsFromDataStore(project_neo.id) } returns project_neo
        getProjectById(project_neo.id) shouldBe project_neo
    }

}
