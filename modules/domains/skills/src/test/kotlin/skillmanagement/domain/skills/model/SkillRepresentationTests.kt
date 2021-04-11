package skillmanagement.domain.skills.model

import org.junit.jupiter.api.Nested
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest

internal class SkillRepresentationTests {

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<SkillRepresentation>() {
        override val serializationExamples = listOf(
            skill_representation_kotlin to skill_representation_kotlin_json,
            skill_representation_java to skill_representation_java_json,
            skill_representation_python to skill_representation_python_json,
        )
    }

}
