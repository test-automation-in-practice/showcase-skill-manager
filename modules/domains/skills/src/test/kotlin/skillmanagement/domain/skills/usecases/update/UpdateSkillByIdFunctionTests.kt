package skillmanagement.domain.skills.usecases.update

import io.kotlintest.shouldBe
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.retry.annotation.EnableRetry
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.common.failure
import skillmanagement.common.success
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.SkillDescription
import skillmanagement.domain.skills.model.SkillEntity
import skillmanagement.domain.skills.model.SkillLabel
import skillmanagement.domain.skills.model.SkillUpdatedEvent
import skillmanagement.domain.skills.model.Tag
import skillmanagement.domain.skills.model.skillId
import skillmanagement.domain.skills.usecases.read.GetSkillByIdFunction
import skillmanagement.domain.skills.usecases.update.SkillUpdateFailure.SkillNotChanged
import skillmanagement.domain.skills.usecases.update.SkillUpdateFailure.SkillNotFound
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.UnitTest
import skillmanagement.test.instant

internal class UpdateSkillByIdFunctionTests {

    private val id = skillId()
    private val skill = SkillEntity(
        id = id,
        version = 2,
        data = Skill(
            label = SkillLabel("Old Label"),
            description = SkillDescription("Old Description"),
            tags = sortedSetOf(Tag("old"))
        ),
        lastUpdate = instant("2020-07-16T12:34:56.789Z")
    )

    @Nested
    @UnitTest
    @ResetMocksAfterEachTest
    inner class FunctionalTests {

        private val getSkillById: GetSkillByIdFunction = mockk()
        private val updateSkillInDataStore: UpdateSkillInDataStoreFunction = mockk()
        private val publishEvent: PublishEventFunction = mockk(relaxUnitFun = true)

        private val updateSkillById = UpdateSkillByIdFunction(getSkillById, updateSkillInDataStore, publishEvent)

        @Test
        fun `updating an existing skill stores it in the data store and publishes an event`() {
            val change: (Skill) -> (Skill) = {
                it.copy(
                    label = SkillLabel("New Label"),
                    description = SkillDescription("New Description"),
                    tags = sortedSetOf(Tag("new"))
                )
            }

            val expectedChangedSkill = skill.update(change)
            val expectedUpdatedSkill = expectedChangedSkill
                .copy(version = 3, lastUpdate = instant("2020-07-16T12:35:06.789Z"))

            every { getSkillById(id) } returns skill
            every { updateSkillInDataStore(expectedChangedSkill) } answers { simulateUpdate(firstArg()) }

            val result = updateSkillById(id, change)

            result shouldBe success(expectedUpdatedSkill)
            verify { publishEvent(SkillUpdatedEvent(expectedUpdatedSkill)) }
        }

        @Test
        fun `updating a non-existing skill returns skill not found failure`() {
            every { getSkillById(id) } returns null

            val result = updateSkillById(skill.id) { it.copy(label = SkillLabel("New Label")) }

            result shouldBe failure(SkillNotFound)

            verify { updateSkillInDataStore wasNot called }
            verify { publishEvent wasNot called }
        }

        @Test
        fun `not changing anything during the update returns skill not changed failure`() {
            every { getSkillById(id) } returns skill

            val result = updateSkillById(skill.id) { it }

            result shouldBe failure(SkillNotChanged(skill))

            verify { updateSkillInDataStore wasNot called }
            verify { publishEvent wasNot called }
        }

        private fun simulateUpdate(it: SkillEntity) =
            it.copy(version = it.version + 1, lastUpdate = it.lastUpdate.plusSeconds(10))

    }

    @Nested
    @ResetMocksAfterEachTest
    @TechnologyIntegrationTest
    @SpringBootTest(classes = [RetryTestsConfiguration::class])
    inner class RetryTests(
        @Autowired private val getSkillById: GetSkillByIdFunction,
        @Autowired private val updateSkillInDataStore: UpdateSkillInDataStoreFunction,
        @Autowired private val updateSkillById: UpdateSkillByIdFunction
    ) {

        private val change: (Skill) -> Skill = { it.copy(label = SkillLabel("new")) }

        @Test
        fun `operation is retried up to 5 times in case of concurrent update exceptions`() {
            every { getSkillById(id) } returns skill
            every { updateSkillInDataStore(any()) } throws ConcurrentSkillUpdateException()
            assertThrows<ConcurrentSkillUpdateException> {
                updateSkillById(id, change)
            }
            verify(exactly = 5) { getSkillById(id) }
        }

        @Test
        fun `operation does not fail if retrying fixes the problem`() {
            every { getSkillById(id) } returns skill
            every { updateSkillInDataStore(any()) }
                .throws(ConcurrentSkillUpdateException())
                .andThenThrows(ConcurrentSkillUpdateException())
                .andThen(skill)

            updateSkillById(id, change)

            verify(exactly = 3) { getSkillById(id) }
        }

    }

    @EnableRetry
    @Import(UpdateSkillByIdFunction::class)
    private class RetryTestsConfiguration {

        @Bean
        fun getSkillById(): GetSkillByIdFunction = mockk(relaxed = true)

        @Bean
        fun updateSkillInDataStore(): UpdateSkillInDataStoreFunction = mockk(relaxed = true)

        @Bean
        fun publishEvent(): PublishEventFunction = mockk(relaxed = true)

    }

}
