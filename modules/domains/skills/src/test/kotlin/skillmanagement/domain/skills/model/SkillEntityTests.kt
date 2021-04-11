package skillmanagement.domain.skills.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.UnitTest

internal class SkillEntityTests {

    @Nested
    @UnitTest
    inner class FunctionTests {

        @Test
        fun `update replaces skill data`() {
            val actual = skill_kotlin.update { skill ->
                skill.copy(
                    label = SkillLabel("updated label"),
                    description = SkillDescription("updated description"),
                    tags = sortedSetOf(Tag("updated"))
                )
            }
            val expected = skill_kotlin.copy(
                data = Skill(
                    label = SkillLabel("updated label"),
                    description = SkillDescription("updated description"),
                    tags = sortedSetOf(Tag("updated"))
                )
            )
            assertThat(actual).isEqualTo(expected)
        }

    }

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<SkillEntity>() {
        override val serializationExamples = listOf(
            skill_kotlin to skill_kotlin_json,
            skill_java to skill_java_json,
            skill_python to skill_python_json
        )
    }

}
