package skillmanagement.domain.projects.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.UnitTest

internal class ProjectEntityTests {

    @Nested
    @UnitTest
    inner class FunctionTests {

        @Test
        fun `update replaces project data`() {
            val actual = project_neo.update { skill ->
                skill.copy(
                    label = ProjectLabel("updated label"),
                    description = ProjectDescription("updated description")
                )
            }
            val expected = project_neo.copy(
                data = Project(
                    label = ProjectLabel("updated label"),
                    description = ProjectDescription("updated description")
                )
            )
            assertThat(actual).isEqualTo(expected)
        }

    }

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<ProjectEntity>() {
        override val serializationExamples = listOf(
            project_neo to project_neo_json,
            project_morpheus to project_morpheus_json,
            project_orbis to project_orbis_json
        )
    }

}
