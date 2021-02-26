package skillmanagement.domain.employees.model

import skillmanagement.common.model.Label
import skillmanagement.common.model.Text
import skillmanagement.test.uuid
import java.time.Instant
import java.time.LocalDate

val skill_kotlin = SkillData(
    id = uuid("3f7985b9-f5f0-4662-bda9-1dcde01f5f3b"),
    label = "Kotlin"
)
val skill_kotlin_knowledge = SkillKnowledge(
    skill = skill_kotlin,
    level = SkillLevel(7),
    secret = false
)

val skill_python = SkillData(
    id = uuid("6935e550-d041-418a-9070-e37431069232"),
    label = "Python"
)
val skill_python_knowledge = SkillKnowledge(
    skill = skill_python,
    level = SkillLevel(5),
    secret = true
)

val project_starlink = ProjectData(
    id = uuid("15c1c1d8-3445-4d95-a945-605aaf087078"),
    label = "Starlink satellite",
    description = "Lorem ipsum dolor sit amet .."
)
val project_starlink_assignment = ProjectAssignment(
    id = uuid("420d3277-a4a9-4a35-9d57-d150b26edc91"),
    project = project_starlink,
    contribution = ProjectContribution("... sed do eiusmod tempor incididunt ut ..."),
    startDate = LocalDate.parse("2018-01-01"),
    endDate = LocalDate.parse("2020-01-31")
)

val project_demo_2 = ProjectData(
    id = uuid("d61c7840-7be3-4668-ae48-6c0b396268b5"),
    label = "Demo-2",
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed .."
)
val project_demo_2_assignment = ProjectAssignment(
    id = uuid("aebf0a2a-4309-4610-829d-13ce0b036496"),
    project = project_demo_2,
    contribution = ProjectContribution("... eu fugiat nulla pariatur. Excepteur sint occaeca ..."),
    startDate = LocalDate.parse("2020-02-01"),
    endDate = null
)

val employee_max_mustermann = Employee(
    id = uuid("eb9b6560-9061-424a-87fd-350327b64d13"),
    version = 1,
    firstName = FirstName("Max"),
    lastName = LastName("Mustermann"),
    title = JobTitle("Managing Consultant"),
    email = EmailAddress("max.mustermann@example-gmbh.de"),
    telephone = TelephoneNumber("+49 555 123456"),
    skills = listOf(skill_kotlin_knowledge, skill_python_knowledge),
    projects = listOf(project_starlink_assignment, project_demo_2_assignment),
    lastUpdate = Instant.parse("2020-06-04T12:34:56.789Z")
)
