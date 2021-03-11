package skillmanagement.domain.projects.model

import org.junit.jupiter.api.Nested
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest

internal class ProjectTests {

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<Project>() {
        override val serializationExamples = listOf(
            project_neo to project_neo_json,
            project_morpheus to project_morpheus_json,
            project_orbis to project_orbis_json
        )
    }

}
