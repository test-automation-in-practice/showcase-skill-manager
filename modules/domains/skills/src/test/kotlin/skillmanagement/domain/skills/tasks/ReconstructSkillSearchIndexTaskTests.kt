package skillmanagement.domain.skills.tasks

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.testit.testutils.logrecorder.api.LogRecord
import org.testit.testutils.logrecorder.junit5.RecordLoggers
import skillmanagement.common.searchindices.SearchIndexAdmin
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.skill_java
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.domain.skills.model.skill_python
import skillmanagement.domain.skills.usecases.read.GetSkillsFromDataStoreFunction
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest
import skillmanagement.test.tasks.AbstractReconstructSearchIndexTaskTests

@UnitTest
@ResetMocksAfterEachTest
internal class ReconstructSkillSearchIndexTaskTests : AbstractReconstructSearchIndexTaskTests<Skill>() {

    private val getSkillsFromDataStore: GetSkillsFromDataStoreFunction = mockk()

    override val searchIndex: SearchIndexAdmin<Skill> = mockk(relaxUnitFun = true)
    override val cut = ReconstructSkillSearchIndexTask(searchIndex, getSkillsFromDataStore)

    override val instance1 = skill_kotlin
    override val instance2 = skill_java
    override val instance3 = skill_python

    @Test
    @RecordLoggers(ReconstructSkillSearchIndexTask::class)
    override fun `task steps are logged`(log: LogRecord) = executeTaskStepsAreLoggedTest(log)

    override fun stubDataStoreToBeEmpty() = stubDataStoreToContain(emptyList())

    override fun stubDataStoreToContain(instances: Collection<Skill>) {
        every { getSkillsFromDataStore(any<(Skill) -> Unit>()) } answers {
            instances.forEach { firstArg<(Skill) -> Unit>()(it) }
        }
    }

    override fun expectedShortDescription(instance: Skill) = "${instance.id} - ${instance.label}"

}
