package skillmanagement.runtime

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import skillmanagement.domain.employees.model.EmailAddress
import skillmanagement.domain.employees.model.EmployeeCreationData
import skillmanagement.domain.employees.model.FirstName
import skillmanagement.domain.employees.model.JobTitle
import skillmanagement.domain.employees.model.LastName
import skillmanagement.domain.employees.model.TelephoneNumber
import skillmanagement.domain.employees.usecases.create.CreateEmployeeFunction
import skillmanagement.domain.employees.usecases.projectassignments.create.CreateProjectAssignmentForEmployeeFunction
import skillmanagement.domain.employees.usecases.skillknowledge.set.SetSkillKnowledgeOfEmployeeFunction
import skillmanagement.domain.projects.model.ProjectCreationData
import skillmanagement.domain.projects.model.ProjectDescription
import skillmanagement.domain.projects.model.ProjectLabel
import skillmanagement.domain.projects.usecases.create.CreateProjectFunction
import skillmanagement.domain.skills.model.SkillCreationData
import skillmanagement.domain.skills.model.SkillDescription
import skillmanagement.domain.skills.model.SkillLabel
import skillmanagement.domain.skills.model.Tag
import skillmanagement.domain.skills.usecases.create.CreateSkillFunction
import java.time.Clock

@Component
@Profile("with-test-data")
class TestDataInserter(
    private val clock: Clock,
    private val createSkill: CreateSkillFunction,
    private val createProject: CreateProjectFunction,
    private val createEmployee: CreateEmployeeFunction,
    private val setSkillKnowledgeOfEmployee: SetSkillKnowledgeOfEmployeeFunction,
    private val createProjectAssignmentForEmployee: CreateProjectAssignmentForEmployeeFunction
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        val kotlin = createSkill(
            SkillCreationData(
                label = SkillLabel("Kotlin"),
                description = SkillDescription("Lorem Ipsum .."),
                tags = sortedSetOf(Tag("language"), Tag("cool"))
            )
        )
        val starlink = createProject(
            ProjectCreationData(
                label = ProjectLabel("Starlink"),
                description = ProjectDescription("Lorem ipsum ..")
            )
        )
        val maxMustermann = createEmployee(
            EmployeeCreationData(
                firstName = FirstName("Max"),
                lastName = LastName("Mustermann"),
                title = JobTitle("Managing Consultant"),
                email = EmailAddress("max.mustermann@example-gmbh.de"),
                telephone = TelephoneNumber("+49 555 123456")
            )
        )
    }

}
