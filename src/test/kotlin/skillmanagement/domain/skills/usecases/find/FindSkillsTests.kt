package skillmanagement.domain.skills.usecases.find

import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.domain.skills.model.skill_python
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
    fun `NoOpQuery just returns all skills from database`() {
        every { findAllSkillsInDataStore() } returns listOf(skill_kotlin, skill_python)
        findSkills(NoOpQuery) shouldBe listOf(skill_kotlin, skill_python)
    }

    @Test
    fun `SkillsMatchingQuery gets IDs from search index and then corresponding skills from database`() {
        val query = SkillsMatchingQuery("kotlin")
        every { searchIndex.query("kotlin") } returns listOf(skill_kotlin.id)
        every { getSkillFromDataStore(listOf(skill_kotlin.id)) } returns mapOf(skill_kotlin.id to skill_kotlin)
        findSkills(query) shouldBe listOf(skill_kotlin)
    }

}
