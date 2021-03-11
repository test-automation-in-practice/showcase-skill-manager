package skillmanagement.domain.projects.model

import org.junit.jupiter.api.Nested
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest

internal class ProjectEventTests {

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<ProjectEvent>() {
        override val serializationExamples = listOf(
            ProjectAddedEvent(project_neo) to """{ "project": $project_neo_json }""",
            ProjectUpdatedEvent(project_morpheus) to """{ "project": $project_morpheus_json }""",
            ProjectDeletedEvent(project_orbis) to """{ "project": $project_orbis_json }"""
        )
    }

}
