package skillmanagement.domain.projects.model

import org.junit.jupiter.api.Nested
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest

internal class ProjectRepresentationTests {

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<ProjectRepresentation>() {
        override val serializationExamples = listOf(
            project_representation_neo to project_representation_neo_json,
            project_representation_morpheus to project_representation_morpheus_json,
            project_representation_orbis to project_representation_orbis_json,
        )
    }

}
