package skillmanagement.domain.employees.find

import io.kotlintest.shouldBe
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import skillmanagement.domain.employees.employee_max_mustermann
import skillmanagement.domain.employees.project_demo_2_assignment
import skillmanagement.domain.employees.project_starlink_assignment
import skillmanagement.domain.employees.skill_kotlin_knowledge
import skillmanagement.domain.employees.skill_python_knowledge
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest

@UnitTest
@ResetMocksAfterEachTest
internal class FindEmployeesTests {

    val findEmployeesInDataStore: FindEmployeesInDataStore = mockk()
    val findEmployeeSkillsInDataStore: FindEmployeeSkillsInDataStore = mockk()
    val findEmployeeProjectsInDataStore: FindEmployeeProjectsInDataStore = mockk()

    val findEmployees = FindEmployees(
        findEmployeesInDataStore = findEmployeesInDataStore,
        findEmployeeSkillsInDataStore = findEmployeeSkillsInDataStore,
        findEmployeeProjectsInDataStore = findEmployeeProjectsInDataStore
    )

    val baseEmployee = employee_max_mustermann.copy(skills = null, projects = null)
    val employeeId = baseEmployee.id

    @Test
    fun `returns empty list of no employees were found`() {
        every { findEmployeesInDataStore() } returns emptyList()

        findEmployees() shouldBe emptyList()

        verify { findEmployeeSkillsInDataStore wasNot called }
        verify { findEmployeeProjectsInDataStore wasNot called }
    }

    @Test
    fun `expandable data is not included by default`() {
        every { findEmployeesInDataStore() } returns listOf(baseEmployee)

        findEmployees() shouldBe listOf(baseEmployee)

        verify { findEmployeeSkillsInDataStore wasNot called }
        verify { findEmployeeProjectsInDataStore wasNot called }
    }

    @Test
    fun `skills can be loaded in addition to base employee data`() {
        val expandedEmployee = baseEmployee.copy(skills = listOf(skill_kotlin_knowledge))

        every { findEmployeesInDataStore() } returns listOf(baseEmployee)
        every { findEmployeeSkillsInDataStore(listOf(employeeId)) } returns
            mapOf(employeeId to listOf(skill_kotlin_knowledge))

        findEmployees(includeSkills = true) shouldBe listOf(expandedEmployee)

        verify { findEmployeeProjectsInDataStore wasNot called }
    }

    @Test
    fun `skills are not loaded if no employees were found`() {
        every { findEmployeesInDataStore() } returns emptyList()

        findEmployees(includeSkills = true) shouldBe emptyList()

        verify { findEmployeeSkillsInDataStore wasNot called }
    }

    @Test
    fun `skills are empty if there are non for an employee`() {
        every { findEmployeesInDataStore() } returns listOf(baseEmployee)
        every { findEmployeeSkillsInDataStore(listOf(employeeId)) } returns emptyMap()

        findEmployees(includeSkills = true) shouldBe listOf(baseEmployee.copy(skills = emptyList()))

        verify { findEmployeeSkillsInDataStore(listOf(employeeId)) }
    }

    @Test
    fun `projects can be loaded in addition to base employee data`() {
        val expandedEmployee = baseEmployee.copy(projects = listOf(project_starlink_assignment))

        every { findEmployeesInDataStore() } returns listOf(baseEmployee)
        every { findEmployeeProjectsInDataStore(listOf(employeeId)) } returns
            mapOf(employeeId to listOf(project_starlink_assignment))

        findEmployees(includeProjects = true) shouldBe listOf(expandedEmployee)

        verify { findEmployeeSkillsInDataStore wasNot called }
    }

    @Test
    fun `projects are not loaded if no employees were found`() {
        every { findEmployeesInDataStore() } returns emptyList()

        findEmployees(includeProjects = true) shouldBe emptyList()

        verify { findEmployeeProjectsInDataStore wasNot called }
    }

    @Test
    fun `projects are empty if there are non for an employee`() {
        every { findEmployeesInDataStore() } returns listOf(baseEmployee)
        every { findEmployeeProjectsInDataStore(listOf(employeeId)) } returns emptyMap()

        findEmployees(includeProjects = true) shouldBe listOf(baseEmployee.copy(projects = emptyList()))

        verify { findEmployeeProjectsInDataStore(listOf(employeeId)) }
    }

    @Test
    fun `different expendable data can be loaded at once`() {
        val expandedEmployee = baseEmployee.copy(
            skills = listOf(skill_python_knowledge),
            projects = listOf(project_demo_2_assignment)
        )

        every { findEmployeesInDataStore() } returns listOf(baseEmployee)
        every { findEmployeeSkillsInDataStore(listOf(employeeId)) } returns
            mapOf(employeeId to listOf(skill_python_knowledge))
        every { findEmployeeProjectsInDataStore(listOf(employeeId)) } returns
            mapOf(employeeId to listOf(project_demo_2_assignment))

        findEmployees(includeSkills = true, includeProjects = true) shouldBe listOf(expandedEmployee)
    }

}
