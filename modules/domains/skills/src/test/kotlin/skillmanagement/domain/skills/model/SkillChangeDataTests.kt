package skillmanagement.domain.skills.model

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.UnitTest
import skillmanagement.test.instant

internal class SkillChangeDataTests {

    @Nested
    @UnitTest
    inner class SkillExtensionFunctionTests {

        private val skill = Skill(
            label = SkillLabel("Changing Data"),
            description = SkillDescription("Much needed for these tests."),
            tags = sortedSetOf(Tag("foo"), Tag("bar"))
        )
        private val changeData = SkillChangeData(
            label = SkillLabel("Changing Data"),
            description = SkillDescription("Much needed for these tests."),
            tags = sortedSetOf(Tag("foo"), Tag("bar")),
        )
        private val changedData = SkillChangeData(
            label = SkillLabel("Changed Data"),
            description = SkillDescription("Completely different."),
            tags = sortedSetOf(Tag("xur")),
        )

        @Test
        fun `toChangeData function creates correct instance`() {
            skill.toChangeData() shouldBe changeData
        }

        @Test
        fun `merge function creates skill with overridden properties`() {
            skill.merge(changedData) shouldBe skill.copy(
                label = changedData.label,
                description = changedData.description,
                tags = changedData.tags
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
