package skillmanagement.domain.skills.model

import org.junit.jupiter.api.Nested
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest

internal class SkillEventTests {

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<SkillEvent>() {
        override val serializationExamples = listOf(
            SkillAddedEvent(skill_kotlin) to """{ "skill": $skill_kotlin_json }""",
            SkillUpdatedEvent(skill_kotlin) to """{ "skill": $skill_kotlin_json }""",
            SkillDeletedEvent(skill_kotlin) to """{ "skill": $skill_kotlin_json }"""
        )
    }

}
