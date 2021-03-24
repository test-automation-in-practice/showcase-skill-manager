package skillmanagement.domain.projects.usecases.delete

import io.kotlintest.shouldBe
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.domain.projects.model.ProjectDeletedEvent
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.domain.projects.usecases.delete.DeleteProjectByIdResult.ProjectNotFound
import skillmanagement.domain.projects.usecases.delete.DeleteProjectByIdResult.SuccessfullyDeleted
import skillmanagement.domain.projects.usecases.read.GetProjectByIdFunction
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class DeleteProjectByIdFunctionTests {

    private val getProjectById: GetProjectByIdFunction = mockk()
    private val deleteProjectFromDataStore: DeleteProjectFromDataStoreFunction = mockk(relaxUnitFun = true)
    private val publishEvent: PublishEventFunction = mockk(relaxUnitFun = true)
    private val deleteProjectById = DeleteProjectByIdFunction(getProjectById, deleteProjectFromDataStore, publishEvent)

    @Test
    fun `given project with ID does not exist when deleting by ID then the result will be ProjectNotFound`() {
        every { getProjectById(project_neo.id) } returns null

        deleteProjectById(project_neo.id) shouldBe ProjectNotFound

        verify { deleteProjectFromDataStore wasNot called }
        verify { publishEvent wasNot called }
    }

    @Test
    fun `given project with ID exists when deleting by ID then the result will be SuccessfullyDeleted`() {
        every { getProjectById(project_neo.id) } returns project_neo

        deleteProjectById(project_neo.id) shouldBe SuccessfullyDeleted

        verify { deleteProjectFromDataStore(project_neo.id) }
        verify { publishEvent(ProjectDeletedEvent(project_neo)) }
    }

}
