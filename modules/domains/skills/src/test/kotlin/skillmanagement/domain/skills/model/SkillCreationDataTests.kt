package skillmanagement.domain.skills.model

import org.junit.jupiter.api.Nested
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest

internal class SkillCreationDataTests {

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<SkillCreationData>() {
        override val serializationExamples = listOf(
            skill_creation_data_kotlin to skill_creation_data_kotlin_json,
            skill_creation_data_java to skill_creation_data_java_json,
            skill_creation_data_python to skill_creation_data_python_json,
        )
    }

}
