package skillmanagement.domain.projects.model

import org.junit.jupiter.api.Nested
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest

internal class ProjectResourceTests {

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<ProjectResource>() {
        override val serializationExamples = listOf(
            project_resource_neo to project_resource_neo_json,
            project_resource_morpheus to project_resource_morpheus_json,
            project_resource_orbis to project_resource_orbis_json,
        )
    }

}
