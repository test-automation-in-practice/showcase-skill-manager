package skillmanagement.domain.skills.model

import org.junit.jupiter.api.Nested
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.uuid

internal class SkillResourceTests {

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<SkillResource>() {
        val instance = SkillResource(
            id = uuid("77e3a78e-c0e3-4272-83f1-a3f4e07326a1"),
            label = SkillLabel("Kotlin"),
            tags = sortedSetOf(Tag("language"), Tag("cool"))
        )
        val json = """
            {
              "id": "77e3a78e-c0e3-4272-83f1-a3f4e07326a1",
              "label": "Kotlin",
              "tags": [
                "cool",
                "language"
              ],
              "links": []
            }
            """

        override val serializationExamples = listOf(instance to json)

    }

}
