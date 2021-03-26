package skillmanagement.domain.employees.usecases.create

import io.kotlintest.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.util.IdGenerator
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.domain.employees.model.EmailAddress
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.EmployeeAddedEvent
import skillmanagement.domain.employees.model.EmployeeCreationData
import skillmanagement.domain.employees.model.FirstName
import skillmanagement.domain.employees.model.JobTitle
import skillmanagement.domain.employees.model.LastName
import skillmanagement.domain.employees.model.TelephoneNumber
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest
import skillmanagement.test.fixedClock
import skillmanagement.test.instant
import skillmanagement.test.uuid

@UnitTest
@ResetMocksAfterEachTest
internal class CreateEmployeeFunctionTests {

    private val idGenerator: IdGenerator = mockk()
    private val insertEmployeeIntoDataStore: InsertEmployeeIntoDataStoreFunction = mockk(relaxUnitFun = true)
    private val publishEvent: PublishEventFunction = mockk(relaxUnitFun = true)
    private val clock = fixedClock("2021-03-24T12:34:56.789Z")

    private val createEmployee = CreateEmployeeFunction(idGenerator, insertEmployeeIntoDataStore, publishEvent, clock)

    @Test
    fun `correct Skill instance is constructed and stored for min data`() {
        every { idGenerator.generateId() } returns uuid("9f3d85ac-1571-4404-af3a-6fda482a6c23")

        val actual = createEmployee(
            EmployeeCreationData(
                firstName = FirstName("Jane"),
                lastName = LastName("Doe"),
                title = JobTitle("Senior Software Engineer"),
                email = EmailAddress("jane.doe@example.com"),
                telephone = TelephoneNumber("+49 123 456789")
            )
        )
        val expected = Employee(
            id = uuid("9f3d85ac-1571-4404-af3a-6fda482a6c23"),
            version = 1,
            firstName = FirstName("Jane"),
            lastName = LastName("Doe"),
            title = JobTitle("Senior Software Engineer"),
            email = EmailAddress("jane.doe@example.com"),
            telephone = TelephoneNumber("+49 123 456789"),
            lastUpdate = instant("2021-03-24T12:34:56.789Z")
        )
        actual shouldBe expected

        verify {
            insertEmployeeIntoDataStore(expected)
            publishEvent(EmployeeAddedEvent(expected))
        }
        confirmVerified(insertEmployeeIntoDataStore, publishEvent)
    }

}
