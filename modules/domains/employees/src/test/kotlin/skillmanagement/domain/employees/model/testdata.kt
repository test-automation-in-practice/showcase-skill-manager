package skillmanagement.domain.employees.model

import skillmanagement.common.model.Suggestion
import skillmanagement.test.instant
import skillmanagement.test.localDate
import skillmanagement.test.uuid
import skillmanagement.test.yearMonth

// Skill Knowledge

internal val skill_kotlin = SkillData(
    id = uuid("3f7985b9-f5f0-4662-bda9-1dcde01f5f3b"),
    label = "Kotlin"
)
internal val skill_knowledge_kotlin = SkillKnowledge(
    skill = skill_kotlin,
    level = SkillLevel(8),
    secret = false
)

internal val skill_java = SkillData(
    id = uuid("f8948935-dab6-4c33-80d0-9f66ae546a7c"),
    label = "Java"
)
internal val skill_knowledge_java = SkillKnowledge(
    skill = skill_java,
    level = SkillLevel(7),
    secret = false
)

internal val skill_python = SkillData(
    id = uuid("6935e550-d041-418a-9070-e37431069232"),
    label = "Python"
)
internal val skill_knowledge_python = SkillKnowledge(
    skill = skill_python,
    level = SkillLevel(4),
    secret = true
)

// Project Assignments

internal val project_neo = ProjectData(
    id = uuid("f804d83f-466c-4eab-a58f-4b25ca1778f3"),
    label = "Neo",
    description = "The PlayStation 4 Pro."
)
internal val project_assignment_neo = ProjectAssignment(
    id = uuid("c35af600-4457-4a99-a40d-9570f339d284"),
    project = project_neo,
    contribution = ProjectContribution("Lorem ipsum dolor sit amet, consectetur adipiscing elit ..."),
    startDate = localDate("2018-01-01"),
    endDate = localDate("2020-01-31")
)

internal val project_orbis = ProjectData(
    id = uuid("dce233f1-7c20-4250-817e-6676485ddb6e"),
    label = "Orbis",
    description = "The PlayStation 4."
)
internal val project_assignment_orbis = ProjectAssignment(
    id = uuid("b825f016-aa79-4eb8-a896-ae3627efdab7"),
    project = project_orbis,
    contribution = ProjectContribution("... sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."),
    startDate = localDate("2009-03-16"),
    endDate = localDate("2010-10-31")
)

internal val project_morpheus = ProjectData(
    id = uuid("d5370813-a4cb-42d5-9d28-ce624c718538"),
    label = "Morpheus",
    description = "The PlayStation VR Headset."
)
internal val project_assignment_morpheus = ProjectAssignment(
    id = uuid("bb125fd7-29bc-4661-bf4f-25d7f239801e"),
    project = project_morpheus,
    contribution = ProjectContribution("Ut enim ad minim veniam ..."),
    startDate = localDate("2019-02-01"),
    endDate = null
)

// Example #1

/** All possible properties are set (max example). */
internal val employee_jane_doe = Employee(
    id = uuid("9e1ff73e-0f66-4b86-8548-040d4016bfc9"),
    version = 1,
    firstName = FirstName("Jane"),
    lastName = LastName("Doe"),
    title = JobTitle("Senior Software Engineer"),
    email = EmailAddress("jane.doe@example.com"),
    telephone = TelephoneNumber("+49 123 456789"),
    description = EmployeeDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit ..."),
    academicDegrees = listOf(
        AcademicDegree(
            subject = AcademicSubject("Computer Science"),
            title = AcademicTitle("Dr."),
            institution = AcademicInstitution("MIT")
        )
    ),
    certifications = listOf(
        Certification("CPSA Advanced Level"),
        Certification("CPSA Foundation Level")
    ),
    publications = listOf(
        Publication("https://github.com/jane_doe11"),
        Publication("Lorem ipsum dolor (Lorem Magazine, 2021)")
    ),
    languages = listOf(
        LanguageProficiency(
            language = Language("English"),
            qualifier = LanguageQualifier("native speaker")
        ),
        LanguageProficiency(
            language = Language("German"),
            qualifier = LanguageQualifier("B2")
        )
    ),
    jobHistory = listOf(
        Job(
            employer = Employer("Example AG"),
            title = JobTitle("Senior Software Engineer"),
            start = yearMonth("2020-01"),
            end = null
        ),
        Job(
            employer = Employer("Example AG"),
            title = JobTitle("Software Engineer"),
            start = yearMonth("2017-11"),
            end = yearMonth("2019-12")
        )
    ),
    skills = listOf(
        skill_knowledge_kotlin,
        skill_knowledge_python
    ),
    projects = listOf(
        project_assignment_neo,
        project_assignment_morpheus
    ),
    lastUpdate = instant("2021-03-26T12:34:56.789Z")
)
internal val employee_jane_doe_json = """
    {
      "id": "9e1ff73e-0f66-4b86-8548-040d4016bfc9",
      "version": 1,
      "firstName": "Jane",
      "lastName": "Doe",
      "title": "Senior Software Engineer",
      "email": "jane.doe@example.com",
      "telephone": "+49 123 456789",
      "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit ...",
      "academicDegrees": [
        {
          "subject": "Computer Science",
          "title": "Dr.",
          "institution": "MIT"
        }
      ],
      "certifications": [
        "CPSA Advanced Level",
        "CPSA Foundation Level"
      ],
      "publications": [
        "https://github.com/jane_doe11",
        "Lorem ipsum dolor (Lorem Magazine, 2021)"
      ],
      "languages": [
        {
          "language": "English",
          "qualifier": "native speaker"
        },
        {
          "language": "German",
          "qualifier": "B2"
        }
      ],
      "jobHistory": [
        {
          "employer": "Example AG",
          "title": "Senior Software Engineer",
          "start": "2020-01"
        },
        {
          "employer": "Example AG",
          "title": "Software Engineer",
          "start": "2017-11",
          "end": "2019-12"
        }
      ],
      "skills": [
        {
          "skill": {
            "id": "3f7985b9-f5f0-4662-bda9-1dcde01f5f3b",
            "label": "Kotlin"
          },
          "level": 8,
          "secret": false
        },
        {
          "skill": {
            "id": "6935e550-d041-418a-9070-e37431069232",
            "label": "Python"
          },
          "level": 4,
          "secret": true
        }
      ],
      "projects": [
        {
          "id": "c35af600-4457-4a99-a40d-9570f339d284",
          "project": {
            "id": "f804d83f-466c-4eab-a58f-4b25ca1778f3",
            "label": "Neo",
            "description": "The PlayStation 4 Pro."
          },
          "contribution": "Lorem ipsum dolor sit amet, consectetur adipiscing elit ...",
          "startDate": "2018-01-01",
          "endDate": "2020-01-31"
        },
        {
          "id": "bb125fd7-29bc-4661-bf4f-25d7f239801e",
          "project": {
            "id": "d5370813-a4cb-42d5-9d28-ce624c718538",
            "label": "Morpheus",
            "description": "The PlayStation VR Headset."
          },
          "contribution": "Ut enim ad minim veniam ...",
          "startDate": "2019-02-01"
        }
      ],
      "lastUpdate": "2021-03-26T12:34:56.789Z"
    }
    """.trimIndent()

internal val employee_resource_jane_doe = employee_jane_doe.toResourceWithoutLinks()
internal val employee_resource_jane_doe_json = """
    {
      "id": "9e1ff73e-0f66-4b86-8548-040d4016bfc9",
      "firstName": "Jane",
      "lastName": "Doe",
      "title": "Senior Software Engineer",
      "email": "jane.doe@example.com",
      "telephone": "+49 123 456789",
      "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit ...",
      "academicDegrees": [
        {
          "subject": "Computer Science",
          "title": "Dr.",
          "institution": "MIT"
        }
      ],
      "certifications": [
        "CPSA Advanced Level",
        "CPSA Foundation Level"
      ],
      "publications": [
        "https://github.com/jane_doe11",
        "Lorem ipsum dolor (Lorem Magazine, 2021)"
      ],
      "languages": [
        {
          "language": "English",
          "qualifier": "native speaker"
        },
        {
          "language": "German",
          "qualifier": "B2"
        }
      ],
      "jobHistory": [
        {
          "employer": "Example AG",
          "title": "Senior Software Engineer",
          "start": "2020-01"
        },
        {
          "employer": "Example AG",
          "title": "Software Engineer",
          "start": "2017-11",
          "end": "2019-12"
        }
      ],
      "skills": [
        {
          "label": "Kotlin",
          "level": 8,
          "secret": false,
          "links": []
        },
        {
          "label": "Python",
          "level": 4,
          "secret": true,
          "links": []
        }
      ],
      "projects": [
        {
          "label": "Neo",
          "description": "The PlayStation 4 Pro.",
          "contribution": "Lorem ipsum dolor sit amet, consectetur adipiscing elit ...",
          "startDate": "2018-01-01",
          "endDate": "2020-01-31",
          "links": []
        },
        {
          "label": "Morpheus",
          "description": "The PlayStation VR Headset.",
          "contribution": "Ut enim ad minim veniam ...",
          "startDate": "2019-02-01",
          "links": []
        }
      ],
      "links": []
    }
    """.trimIndent()

internal val employee_creation_data_jane_doe = employee_jane_doe.toCreationData()
internal val employee_creation_data_jane_doe_json = """
    {
      "firstName": "Jane",
      "lastName": "Doe",
      "title": "Senior Software Engineer",
      "email": "jane.doe@example.com",
      "telephone": "+49 123 456789"
    }
    """.trimIndent()

internal val employee_change_data_jane_doe = employee_jane_doe.toChangeData()
internal val employee_change_data_jane_doe_json = """
    {
      "firstName": "Jane",
      "lastName": "Doe",
      "title": "Senior Software Engineer",
      "email": "jane.doe@example.com",
      "telephone": "+49 123 456789",
      "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit ...",
      "academicDegrees": [
        {
          "subject": "Computer Science",
          "title": "Dr.",
          "institution": "MIT"
        }
      ],
      "certifications": [
        "CPSA Advanced Level",
        "CPSA Foundation Level"
      ],
      "publications": [
        "https://github.com/jane_doe11",
        "Lorem ipsum dolor (Lorem Magazine, 2021)"
      ],
      "languages": [
        {
          "language": "English",
          "qualifier": "native speaker"
        },
        {
          "language": "German",
          "qualifier": "B2"
        }
      ],
      "jobHistory": [
        {
          "employer": "Example AG",
          "title": "Senior Software Engineer",
          "start": "2020-01"
        },
        {
          "employer": "Example AG",
          "title": "Software Engineer",
          "start": "2017-11",
          "end": "2019-12"
        }
      ]
    }
    """.trimIndent()

internal val employee_suggestion_jane_doe = employee_jane_doe.toSuggestion()

// Example #2

/** Only required and some optional properties are set (medium example). */
internal val employee_john_doe = Employee(
    id = uuid("0370f159-2d3b-4e40-9438-10ff34dd62c5"),
    version = 1,
    firstName = FirstName("John"),
    lastName = LastName("Doe"),
    title = JobTitle("Hardware Engineer"),
    email = EmailAddress("john.doe@example.com"),
    telephone = TelephoneNumber("+49 123 987654"),
    description = EmployeeDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit ..."),
    languages = listOf(
        LanguageProficiency(
            language = Language("English"),
            qualifier = LanguageQualifier("native speaker")
        )
    ),
    jobHistory = listOf(
        Job(
            employer = Employer("Example AG"),
            title = JobTitle("Consultant"),
            start = yearMonth("2009-03"),
            end = null
        )
    ),
    skills = listOf(skill_knowledge_java),
    projects = listOf(project_assignment_orbis),
    lastUpdate = instant("2021-03-25T12:34:56.789Z")
)
internal val employee_john_doe_json = """
    {
      "id": "0370f159-2d3b-4e40-9438-10ff34dd62c5",
      "version": 1,
      "firstName": "John",
      "lastName": "Doe",
      "title": "Hardware Engineer",
      "email": "john.doe@example.com",
      "telephone": "+49 123 987654",
      "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit ...",
      "academicDegrees": [],
      "certifications": [],
      "publications": [],
      "languages": [
        {
          "language": "English",
          "qualifier": "native speaker"
        }
      ],
      "jobHistory": [
        {
          "employer": "Example AG",
          "title": "Consultant",
          "start": "2009-03"
        }
      ],
      "skills": [
        {
          "skill": {
            "id": "f8948935-dab6-4c33-80d0-9f66ae546a7c",
            "label": "Java"
          },
          "level": 7,
          "secret": false
        }
      ],
      "projects": [
        {
          "id": "b825f016-aa79-4eb8-a896-ae3627efdab7",
          "project": {
            "id": "dce233f1-7c20-4250-817e-6676485ddb6e",
            "label": "Orbis",
            "description": "The PlayStation 4."
          },
          "contribution": "... sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
          "startDate": "2009-03-16",
          "endDate": "2010-10-31"
        }
      ],
      "lastUpdate": "2021-03-25T12:34:56.789Z"
    }
    """.trimIndent()

internal val employee_resource_john_doe = employee_john_doe.toResourceWithoutLinks()
internal val employee_resource_john_doe_json = """
    {
      "id": "0370f159-2d3b-4e40-9438-10ff34dd62c5",
      "firstName": "John",
      "lastName": "Doe",
      "title": "Hardware Engineer",
      "email": "john.doe@example.com",
      "telephone": "+49 123 987654",
      "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit ...",
      "academicDegrees": [],
      "certifications": [],
      "publications": [],
      "languages": [
        {
          "language": "English",
          "qualifier": "native speaker"
        }
      ],
      "jobHistory": [
        {
          "employer": "Example AG",
          "title": "Consultant",
          "start": "2009-03"
        }
      ],
      "skills": [
        {
          "label": "Java",
          "level": 7,
          "secret": false,
          "links": []
        }
      ],
      "projects": [
        {
          "label": "Orbis",
          "description": "The PlayStation 4.",
          "contribution": "... sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
          "startDate": "2009-03-16",
          "endDate": "2010-10-31",
          "links": []
        }
      ],
      "links": []
    }
    """.trimIndent()

internal val employee_creation_data_john_doe = employee_john_doe.toCreationData()
internal val employee_creation_data_john_doe_json = """
    {
      "firstName": "John",
      "lastName": "Doe",
      "title": "Hardware Engineer",
      "email": "john.doe@example.com",
      "telephone": "+49 123 987654"
    }
    """.trimIndent()

internal val employee_change_data_john_doe = employee_john_doe.toChangeData()
internal val employee_change_data_john_doe_json = """
    {
      "firstName": "John",
      "lastName": "Doe",
      "title": "Hardware Engineer",
      "email": "john.doe@example.com",
      "telephone": "+49 123 987654",
      "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit ...",
      "academicDegrees": [],
      "certifications": [],
      "publications": [],
      "languages": [
        {
          "language": "English",
          "qualifier": "native speaker"
        }
      ],
      "jobHistory": [
        {
          "employer": "Example AG",
          "title": "Consultant",
          "start": "2009-03"
        }
      ]
    }
    """.trimIndent()

internal val employee_suggestion_john_doe = employee_john_doe.toSuggestion()

// Example #3

/**
 * Only required properties are set (min example).
 *
 */
internal val employee_john_smith = Employee(
    id = uuid("53b5f462-0c39-4e2a-83bf-aa407cf309be"),
    version = 1,
    firstName = FirstName("John"),
    lastName = LastName("Smith"),
    title = JobTitle("Junior Software Engineer"),
    email = EmailAddress("john.smith@example.com"),
    telephone = TelephoneNumber("+49 123 948675"),
    lastUpdate = instant("2021-03-24T12:34:56.789Z")
)
internal val employee_john_smith_json = """
    {
      "id": "53b5f462-0c39-4e2a-83bf-aa407cf309be",
      "version": 1,
      "firstName": "John",
      "lastName": "Smith",
      "title": "Junior Software Engineer",
      "email": "john.smith@example.com",
      "telephone": "+49 123 948675",
      "academicDegrees": [],
      "certifications": [],
      "publications": [],
      "languages": [],
      "jobHistory": [],
      "skills": [],
      "projects": [],
      "lastUpdate": "2021-03-24T12:34:56.789Z"
    }
    """.trimIndent()

internal val employee_resource_john_smith = employee_john_smith.toResourceWithoutLinks()
internal val employee_resource_john_smith_json = """
    {
      "id": "53b5f462-0c39-4e2a-83bf-aa407cf309be",
      "firstName": "John",
      "lastName": "Smith",
      "title": "Junior Software Engineer",
      "email": "john.smith@example.com",
      "telephone": "+49 123 948675",
      "academicDegrees": [],
      "certifications": [],
      "publications": [],
      "languages": [],
      "jobHistory": [],
      "skills": [],
      "projects": [],
      "links": []
    }
    """.trimIndent()

internal val employee_creation_data_john_smith = employee_john_smith.toCreationData()
internal val employee_creation_data_john_smith_json = """
    {
      "firstName": "John",
      "lastName": "Smith",
      "title": "Junior Software Engineer",
      "email": "john.smith@example.com",
      "telephone": "+49 123 948675"
    }
    """.trimIndent()

internal val employee_change_data_john_smith = employee_john_smith.toChangeData()
internal val employee_change_data_john_smith_json = """
    {
      "firstName": "John",
      "lastName": "Smith",
      "title": "Junior Software Engineer",
      "email": "john.smith@example.com",
      "telephone": "+49 123 948675",
      "academicDegrees": [],
      "certifications": [],
      "publications": [],
      "languages": [],
      "jobHistory": []
    }
    """.trimIndent()

internal val employee_suggestion_john_smith = employee_john_smith.toSuggestion()

// Functions

private fun Employee.toResourceWithoutLinks() =
    EmployeeResource(
        id = id,
        firstName = firstName,
        lastName = lastName,
        title = title,
        email = email,
        telephone = telephone,
        description = description,
        academicDegrees = academicDegrees,
        certifications = certifications,
        publications = publications,
        languages = languages,
        jobHistory = jobHistory,
        skills = skills.map(SkillKnowledge::toResourceWithoutLinks),
        projects = projects.map(ProjectAssignment::toResourceWithoutLinks)
    )

private fun SkillKnowledge.toResourceWithoutLinks() =
    SkillKnowledgeResource(
        label = skill.label,
        level = level,
        secret = secret
    )

private fun ProjectAssignment.toResourceWithoutLinks() =
    ProjectAssignmentResource(
        label = project.label,
        description = project.description,
        contribution = contribution,
        startDate = startDate,
        endDate = endDate
    )

private fun Employee.toCreationData() =
    EmployeeCreationData(
        firstName = firstName,
        lastName = lastName,
        title = title,
        email = email,
        telephone = telephone
    )

internal fun Employee.toSuggestion() =
    Suggestion(
        id = id,
        label = compositeName()
    )

internal fun employee(
    id: String = uuid().toString(),
    version: Int = 1,
    firstName: String = "Jane",
    lastName: String = "Doe",
    title: String = "Senior Software Engineer",
    email: String = "$firstName.$lastName@example.com",
    telephone: String = "+49 123 456789",
    lastUpdate: String = "2021-03-26T12:34:56.789Z"
) = Employee(
    id = uuid(id),
    version = version,
    firstName = FirstName(firstName),
    lastName = LastName(lastName),
    title = JobTitle(title),
    email = EmailAddress(email),
    telephone = TelephoneNumber(telephone),
    lastUpdate = instant(lastUpdate)
)

/**
 * Creates an instance based on this [Employee] which contains only those
 * properties that would be set after creating a new [Employee] based on
 * [EmployeeCreationData].
 */
internal fun Employee.asFreshlyCreatedInstance() =
    Employee(
        id = id,
        version = 1,
        firstName = firstName,
        lastName = lastName,
        title = title,
        email = email,
        telephone = telephone,
        lastUpdate = lastUpdate
    )
