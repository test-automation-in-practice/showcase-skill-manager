package skillmanagement.domain.employees.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.UnitTest
import skillmanagement.test.stringOfLength

internal class EmployeeTests {

    // TODO addOrUpdateXXX tests

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<Employee>() {
        override val serializationExamples = listOf(
            employee_jane_doe to employee_jane_doe_json,
            employee_john_doe to employee_john_doe_json,
            employee_john_smith to employee_john_smith_json
        )
    }

    private val assignment1 = project_assignment_neo
    private val assignment2 = project_assignment_morpheus
    private val projects = listOf(assignment1, assignment2)
    private val knowledge1 = skill_knowledge_kotlin
    private val knowledge2 = skill_knowledge_python
    private val skills = listOf(knowledge1, knowledge2)
    private val employee = employee_jane_doe.copy(projects = projects, skills = skills)

    @Nested
    @UnitTest
    inner class RemoveProjectAssignmentTests {

        @Test
        fun `does not remove anything if nothing matches expression`() {
            val removeNone = employee.removeProjectAssignment { false }
            assertThat(removeNone.projects).isEqualTo(projects)
        }

        @Test
        fun `removes all project assignments if all match expression`() {
            val removeAll = employee.removeProjectAssignment { true }
            assertThat(removeAll.projects).isEmpty()
        }

        @Test
        fun `removes only project assignments that match expression`() {
            val removeSpecific = employee.removeProjectAssignment { it == assignment1 }
            assertThat(removeSpecific.projects).isEqualTo(projects - assignment1)
        }

    }

    @Nested
    @UnitTest
    inner class UpdateProjectAssignmentsByProjectIdTests {

        @Test
        fun `does nothing for unknown project ID`() {
            var invoked = false
            employee.updateProjectAssignment(externalProjectId()) { invoked = true; it }
            assertThat(invoked).isFalse()
        }

        @Test
        fun `updates only relevant assignments`() {
            val newContribution = ProjectContribution(stringOfLength(5))

            val updatedEmployee = employee.updateProjectAssignment(assignment2.project.id) { assignment ->
                assignment.copy(contribution = newContribution)
            }

            assertThat(updatedEmployee.projects)
                .containsOnly(assignment1, assignment2.copy(contribution = newContribution))
        }

    }

    @Nested
    @UnitTest
    inner class RemoveSkillKnowledgeTests {

        @Test
        fun `does not remove anything if nothing matches expression`() {
            val removeNone = employee.removeSkillKnowledge { false }
            assertThat(removeNone.skills).isEqualTo(skills)
        }

        @Test
        fun `removes all skill knowledge if all match expression`() {
            val removeAll = employee.removeSkillKnowledge { true }
            assertThat(removeAll.skills).isEmpty()
        }

        @Test
        fun `removes only skill knowledge that match expression`() {
            val removeSpecific = employee.removeSkillKnowledge { it == knowledge2 }
            assertThat(removeSpecific.skills).isEqualTo(skills - knowledge2)
        }

    }

    @Nested
    @UnitTest
    inner class UpdateSkillKnowledgeBySkillIdTests {

        @Test
        fun `does nothing for unknown skill ID`() {
            var invoked = false
            employee.updateSkillKnowledge(externalSkillId()) { invoked = true; it }
            assertThat(invoked).isFalse()
        }

        @Test
        fun `updates only relevant knowledge`() {
            val newLevel = SkillLevel(1)

            val updatedEmployee = employee.updateSkillKnowledge(knowledge2.skill.id) { knowledge ->
                knowledge.copy(level = newLevel)
            }

            assertThat(updatedEmployee.skills)
                .containsOnly(knowledge1, knowledge2.copy(level = newLevel))
        }

    }

}
