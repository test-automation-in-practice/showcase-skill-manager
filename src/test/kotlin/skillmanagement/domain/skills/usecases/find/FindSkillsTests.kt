package skillmanagement.domain.skills.usecases.find

import io.mockk.mockk
import org.junit.jupiter.api.Test
import skillmanagement.domain.skills.searchindex.SkillSearchIndex
import skillmanagement.domain.skills.usecases.get.GetSkillFromDataStore
import skillmanagement.test.UnitTest

@UnitTest
internal class FindSkillsTests {

    val findAllSkillsInDataStore: FindAllSkillsInDataStore = mockk()
    val getSkillFromDataStore: GetSkillFromDataStore = mockk()
    val searchIndex: SkillSearchIndex = mockk()
    val findSkills = FindSkills(findAllSkillsInDataStore, getSkillFromDataStore, searchIndex)

    @Test
    fun `abc def`() {
        TODO()
    }

}
