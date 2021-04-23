package skillmanagement.domain.projects.model

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.UnitTest

internal class ProjectChangeDataTests {

    @Nested
    @UnitTest
    inner class ProjectExtensionFunctionTests {

        private val project = Project(
            label = ProjectLabel("Changing Data"),
            description = ProjectDescription("Much needed for these tests.")
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
