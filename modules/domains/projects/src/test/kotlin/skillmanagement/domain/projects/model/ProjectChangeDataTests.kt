package skillmanagement.domain.projects.model

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.UnitTest
import skillmanagement.test.instant

internal class ProjectChangeDataTests {

    @Nested
    @UnitTest
    inner class ProjectExtensionFunctionTests {

        private val project = ProjectEntity(
            id = projectId("3039df54-e86f-4973-9887-738510305e48"),
            version = 42,
            label = ProjectLabel("Changing Data"),
            description = ProjectDescription("Much needed for these tests."),
            lastUpdate = instant("2021-03-03T12:34:56.789Z")
        )
        private val changeData = ProjectChangeData(
            label = ProjectLabel("Changing Data"),
            description = ProjectDescription("Much needed for these tests."),
        )
        private val changedData = ProjectChangeData(
            label = ProjectLabel("Changed Data"),
            description = ProjectDescription("Completely different."),
        )

        @Test
        fun `toChangeData function creates correct instance`() {
            project.toChangeData() shouldBe changeData
        }

        @Test
        fun `merge function creates project with overridden properties`() {
            project.merge(changedData) shouldBe project.copy(
                label = changedData.label,
                description = changedData.description
            )
        }

    }

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<ProjectChangeData>() {
        override val serializationExamples = listOf(
            project_change_data_neo to project_change_data_neo_json,
            project_change_data_morpheus to project_change_data_morpheus_json,
            project_change_data_orbis to project_change_data_orbis_json
        )
    }

}
