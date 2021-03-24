package skillmanagement.domain.projects.model

import org.junit.jupiter.api.Nested
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest

internal class ProjectCreationDataTests {

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<ProjectCreationData>() {
        override val serializationExamples = listOf(
            project_creation_data_neo to project_creation_data_neo_json,
            project_creation_data_morpheus to project_creation_data_morpheus_json,
            project_creation_data_orbis to project_creation_data_orbis_json
        )
    }

}
