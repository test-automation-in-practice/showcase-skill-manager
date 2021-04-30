package skillmanagement.domain.employees.model

import io.kotlintest.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.UnitTest
import skillmanagement.test.yearMonth

internal class EmployeeChangeDataTests {

    @Nested
    @UnitTest
    inner class EmployeeExtensionFunctionTests {

        @Test
        fun `toChangeData function creates correct instance`() {
            employee_jane_doe.data.toChangeData() shouldBe EmployeeChangeData(
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
                )
            )
        }

        @Test
        fun `merge function creates employee with overridden properties`() {
            val e1 = employee_john_smith.data // data for only required properties
            val e2 = employee_jane_doe.data // data for all properties

            val cd1 = e1.toChangeData()
            val cd2 = e2.toChangeData()

            // skills and projects are not changed
            e1.merge(cd2) shouldBe e2.copy(skills = e1.skills, projects = e1.projects)
            e2.merge(cd1) shouldBe e1.copy(skills = e2.skills, projects = e2.projects)
        }

    }

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<EmployeeChangeData>() {
        override val serializationExamples = listOf(
            employee_change_data_jane_doe to employee_change_data_jane_doe_json,
            employee_change_data_john_doe to employee_change_data_john_doe_json,
            employee_change_data_john_smith to employee_change_data_john_smith_json
        )
    }

}
