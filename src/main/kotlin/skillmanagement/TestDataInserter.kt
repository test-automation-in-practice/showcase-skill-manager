package skillmanagement

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import skillmanagement.domain.employees.EmailAddress
import skillmanagement.domain.employees.FirstName
import skillmanagement.domain.employees.LastName
import skillmanagement.domain.employees.ProjectContribution
import skillmanagement.domain.employees.SkillLevel
import skillmanagement.domain.employees.TelephoneNumber
import skillmanagement.domain.employees.Title
import skillmanagement.domain.employees.add.AddEmployee
import skillmanagement.domain.employees.projectassignments.create.CreateProjectAssignmentForEmployee
import skillmanagement.domain.employees.skillknowledge.set.SetSkillKnowledgeOfEmployee
import skillmanagement.domain.projects.ProjectDescription
import skillmanagement.domain.projects.ProjectLabel
import skillmanagement.domain.projects.add.AddProject
import skillmanagement.domain.skills.SkillLabel
import skillmanagement.domain.skills.add.AddSkill
import java.time.Clock
import java.time.LocalDate

@Component
@Profile("with-test-data")
class TestDataInserter(
    private val clock: Clock,
    private val addSkill: AddSkill,
    private val addProject: AddProject,
    private val addEmployee: AddEmployee,
    private val setSkillKnowledgeOfEmployee: SetSkillKnowledgeOfEmployee,
    private val createProjectAssignmentForEmployee: CreateProjectAssignmentForEmployee
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        val kotlin = addSkill(SkillLabel("Kotlin"))
        val starlink = addProject(ProjectLabel("Starlink"), ProjectDescription("Lorem ipsum .."))

        val maxMustermann = addEmployee(
            firstName = FirstName("Max"),
            lastName = LastName("Mustermann"),
            title = Title("Managing Consultant"),
            email = EmailAddress("max.mustermann@example-gmbh.de"),
            telephone = TelephoneNumber("+49 555 123456")
        )

        setSkillKnowledgeOfEmployee(
            employeeId = maxMustermann.id,
            skillId = kotlin.id,
            level = SkillLevel(7),
            secret = false
        )
        createProjectAssignmentForEmployee(
            employeeId = maxMustermann.id,
            projectId = starlink.id,
            contribution = ProjectContribution("eu fugiat nulla pariatur. Excepteur sint occaeca"),
            startDate = LocalDate.now(clock).minusDays(180),
            endDate = LocalDate.now(clock).minusDays(10)
        )
    }

}
