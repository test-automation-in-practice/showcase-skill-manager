package skillmanagement.domain.skills.model

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.UnitTest
import skillmanagement.test.instant
import skillmanagement.test.uuid

internal class SkillChangeDataTests {

    @Nested
    @UnitTest
    inner class SkillExtensionFunctionTests {

        private val skill = Skill(
            id = uuid("3039df54-e86f-4973-9887-738510305e48"),
            version = 42,
            label = SkillLabel("Changing Data"),
            description = SkillDescription("Much needed for these tests."),
            tags = sortedSetOf(Tag("foo"), Tag("bar")),
            lastUpdate = instant("2021-03-03T12:34:56.789Z")
        )
        private val changeData = SkillChangeData(
            label = SkillLabel("Changing Data"),
            description = SkillDescription("Much needed for these tests."),
            tags = sortedSetOf(Tag("foo"), Tag("bar")),
        )

        @Test
        fun `toChangeData function creates correct instance`() {
            skill.toChangeData() shouldBe changeData
        }

        @Test
        fun `merge function creates skill with overridden properties`() {
            skill.merge(changeData) shouldBe skill.copy(
                label = changeData.label,
                description = changeData.description,
                tags = changeData.tags
            )
        }

    }

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<SkillChangeData>() {
        override val serializationExamples = listOf(
            skill_change_data_kotlin to skill_change_data_kotlin_json,
            skill_change_data_java to skill_change_data_java_json,
            skill_change_data_python to skill_change_data_python_json,
        )
    }

}
