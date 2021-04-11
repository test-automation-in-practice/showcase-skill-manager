package skillmanagement.domain.projects.tasks

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.testit.testutils.logrecorder.api.LogRecord
import org.testit.testutils.logrecorder.junit5.RecordLoggers
import skillmanagement.common.searchindices.SearchIndexAdmin
import skillmanagement.domain.projects.model.ProjectEntity
import skillmanagement.domain.projects.model.project_morpheus
import skillmanagement.domain.projects.model.project_neo
import skillmanagement.domain.projects.model.project_orbis
import skillmanagement.domain.projects.usecases.read.GetProjectsFromDataStoreFunction
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest
import skillmanagement.test.tasks.AbstractReconstructSearchIndexTaskTests

@UnitTest
@ResetMocksAfterEachTest
internal class ReconstructProjectSearchIndexTaskTests : AbstractReconstructSearchIndexTaskTests<ProjectEntity>() {

    private val getProjectsFromDataStore: GetProjectsFromDataStoreFunction = mockk()

    override val searchIndexAdmin: SearchIndexAdmin<ProjectEntity> = mockk(relaxUnitFun = true)
    override val cut = ReconstructProjectSearchIndexTask(searchIndexAdmin, getProjectsFromDataStore)

    override val instance1 = project_neo
    override val instance2 = project_morpheus
    override val instance3 = project_orbis

    @Test
    @RecordLoggers(ReconstructProjectSearchIndexTask::class)
    override fun `task steps are logged`(log: LogRecord) = executeTaskStepsAreLoggedTest(log)

    override fun stubDataStoreToBeEmpty() = stubDataStoreToContain(emptyList())

    override fun stubDataStoreToContain(instances: Collection<ProjectEntity>) {
        every { getProjectsFromDataStore(any<(ProjectEntity) -> Unit>()) } answers {
            instances.forEach { firstArg<(ProjectEntity) -> Unit>()(it) }
        }
    }

    override fun expectedShortDescription(instance: ProjectEntity) = "${instance.id} - ${instance.label}"

}
