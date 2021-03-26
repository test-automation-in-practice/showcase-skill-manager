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
            employee_jane_doe.toChangeData() shouldBe EmployeeChangeData(
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
            val employee1 = employee_john_smith // data for only required properties
            val employee2 = employee_jane_doe // data for all properties

            val changeData1 = employee1.toChangeData()
            val changeData2 = employee2.toChangeData()

            employee1.merge(changeData2) shouldBe employee2.copy(
                id = employee1.id,
                version = employee1.version,
                skills = employee1.skills,
                projects = employee1.projects,
                lastUpdate = employee1.lastUpdate
            )

            employee2.merge(changeData1) shouldBe employee1.copy(
                id = employee2.id,
                version = employee2.version,
                skills = employee2.skills,
                projects = employee2.projects,
                lastUpdate = employee2.lastUpdate
            )
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
