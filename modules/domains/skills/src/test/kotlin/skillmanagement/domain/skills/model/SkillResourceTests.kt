package skillmanagement.domain.skills.model

import org.junit.jupiter.api.Nested
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest

internal class SkillResourceTests {

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<SkillResource>() {
        override val serializationExamples = listOf(
            skill_resource_kotlin to skill_resource_kotlin_json,
            skill_resource_java to skill_resource_java_json,
            skill_resource_python to skill_resource_python_json,
        )
    }

}
