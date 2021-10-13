package skillmanagement.domain.skills.tasks

import info.novatec.testit.logrecorder.api.LogRecord
import info.novatec.testit.logrecorder.logback.junit5.RecordLoggers
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import skillmanagement.common.searchindices.SearchIndexAdmin
import skillmanagement.domain.skills.model.SkillEntity
import skillmanagement.domain.skills.model.skill_java
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.domain.skills.model.skill_python
import skillmanagement.domain.skills.usecases.read.GetSkillsFromDataStoreFunction
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest
import skillmanagement.test.tasks.AbstractReconstructSearchIndexTaskTests

@UnitTest
@ResetMocksAfterEachTest
internal class ReconstructSkillSearchIndexTaskTests : AbstractReconstructSearchIndexTaskTests<SkillEntity>() {

    private val getSkillsFromDataStore: GetSkillsFromDataStoreFunction = mockk()

    override val searchIndexAdmin: SearchIndexAdmin<SkillEntity> = mockk(relaxUnitFun = true)
    override val cut = ReconstructSkillSearchIndexTask(searchIndexAdmin, getSkillsFromDataStore)

    override val instance1 = skill_kotlin
    override val instance2 = skill_java
    override val instance3 = skill_python

    @Test
    @RecordLoggers(ReconstructSkillSearchIndexTask::class)
    override fun `task steps are logged`(log: LogRecord) = executeTaskStepsAreLoggedTest(log)

    override fun stubDataStoreToBeEmpty() = stubDataStoreToContain(emptyList())

    override fun stubDataStoreToContain(instances: Collection<SkillEntity>) {
        every { getSkillsFromDataStore(any<(SkillEntity) -> Unit>()) } answers {
            instances.forEach { firstArg<(SkillEntity) -> Unit>()(it) }
        }
    }

    override fun expectedShortDescription(instance: SkillEntity) = "${instance.id} - ${instance.data.label}"

}
