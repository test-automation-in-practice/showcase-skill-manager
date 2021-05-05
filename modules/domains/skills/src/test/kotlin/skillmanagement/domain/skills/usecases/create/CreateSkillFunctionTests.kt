package skillmanagement.domain.skills.usecases.create

import io.kotlintest.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.util.IdGenerator
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.SkillAddedEvent
import skillmanagement.domain.skills.model.SkillCreationData
import skillmanagement.domain.skills.model.SkillDescription
import skillmanagement.domain.skills.model.SkillEntity
import skillmanagement.domain.skills.model.SkillLabel
import skillmanagement.domain.skills.model.Tag
import skillmanagement.domain.skills.model.skillId
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest
import skillmanagement.test.fixedClock
import skillmanagement.test.instant
import skillmanagement.test.uuid
import java.util.Collections.emptySortedSet

@UnitTest
@ResetMocksAfterEachTest
internal class CreateSkillFunctionTests {

    private val idGenerator: IdGenerator = mockk()
    private val insertSkillIntoDataStore: InsertSkillIntoDataStoreFunction = mockk(relaxUnitFun = true)
    private val publishEvent: PublishEventFunction = mockk(relaxUnitFun = true)
    private val clock = fixedClock("2020-07-14T12:34:56.789Z")

    private val createSkill = CreateSkillFunction(idGenerator, insertSkillIntoDataStore, publishEvent, clock)

    @Test
    fun `correct Skill instance is constructed and stored for min data`() {
        every { idGenerator.generateId() } returns uuid("312f3bfc-c9b0-4b4c-9cc4-33242cdfc39e")

        val actual = createSkill(
            SkillCreationData(
                label = SkillLabel("Skill #1")
            )
        )
        val expected = SkillEntity(
            id = skillId("312f3bfc-c9b0-4b4c-9cc4-33242cdfc39e"),
            version = 1,
            data = Skill(
                label = SkillLabel("Skill #1"),
                description = null,
                tags = emptySortedSet()
            ),
            created = instant("2020-07-14T12:34:56.789Z"),
            lastUpdate = instant("2020-07-14T12:34:56.789Z")
        )
        actual shouldBe expected

        verify {
            insertSkillIntoDataStore(expected)
            publishEvent(SkillAddedEvent(expected))
        }
        confirmVerified(insertSkillIntoDataStore, publishEvent)
    }

    @Test
    fun `correct Skill instance is constructed and stored for max data`() {
        every { idGenerator.generateId() } returns uuid("da8748e1-405b-456c-92d4-26fdad09a0c9")

        val actual = createSkill(
            SkillCreationData(
                label = SkillLabel("Skill #2"),
                description = SkillDescription("description"),
                tags = sortedSetOf(Tag("foo-bar"))
            )
        )
        val expected = SkillEntity(
            id = skillId("da8748e1-405b-456c-92d4-26fdad09a0c9"),
            version = 1,
            data = Skill(
                label = SkillLabel("Skill #2"),
                description = SkillDescription("description"),
                tags = sortedSetOf(Tag("foo-bar"))
            ),
            created = instant("2020-07-14T12:34:56.789Z"),
            lastUpdate = instant("2020-07-14T12:34:56.789Z")
        )
        actual shouldBe expected

        verify {
            insertSkillIntoDataStore(expected)
            publishEvent(SkillAddedEvent(expected))
        }
        confirmVerified(insertSkillIntoDataStore, publishEvent)
    }

}
