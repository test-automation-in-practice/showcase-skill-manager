package skillmanagement.domain.employees.usecases.update

import io.kotlintest.shouldBe
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.retry.annotation.EnableRetry
import skillmanagement.common.events.PublishEventFunction
import skillmanagement.domain.employees.model.EmailAddress
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.EmployeeUpdatedEvent
import skillmanagement.domain.employees.model.FirstName
import skillmanagement.domain.employees.model.JobTitle
import skillmanagement.domain.employees.model.LastName
import skillmanagement.domain.employees.model.TelephoneNumber
import skillmanagement.domain.employees.usecases.read.GetEmployeeByIdFunction
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.EmployeeNotChanged
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.EmployeeNotFound
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdResult.SuccessfullyUpdated
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.UnitTest
import skillmanagement.test.instant
import skillmanagement.test.uuid

internal class UpdateEmployeeByIdFunctionTests {

    private val id = uuid()
    private val employee = Employee(
        id = id,
        version = 2,
        firstName = FirstName("Old-First-Name"),
        lastName = LastName("Old-Last-Name"),
        title = JobTitle("Old-Title"),
        email = EmailAddress("john.smith@old-example.com"),
        telephone = TelephoneNumber("+49 123 948675"),
        lastUpdate = instant("2020-07-16T12:34:56.789Z")
    )

    @Nested
    @UnitTest
    @ResetMocksAfterEachTest
    inner class FunctionalTests {

        private val getEmployeeById: GetEmployeeByIdFunction = mockk()
        private val updateEmployeeInDataStore: UpdateEmployeeInDataStoreFunction = mockk()
        private val publishEvent: PublishEventFunction = mockk(relaxUnitFun = true)

        private val updateEmployeeById =
            UpdateEmployeeByIdFunction(getEmployeeById, updateEmployeeInDataStore, publishEvent)

        @Test
        fun `updating an existing employee stores it in the data store and publishes an event`() {
            val change: (Employee) -> (Employee) = {
                it.copy(
                    firstName = FirstName("New-First-Name"),
                    lastName = LastName("New-Last-Name"),
                    title = JobTitle("New-Title"),
                    email = EmailAddress("john.smith@new-example.com"),
                    telephone = TelephoneNumber("+49 123 456789")
                )
            }

            val expectedChangedEmployee = change(employee)
            val expectedUpdatedEmployee = expectedChangedEmployee
                .copy(version = 3, lastUpdate = instant("2020-07-16T12:35:06.789Z"))

            every { getEmployeeById(id) } returns employee
            every { updateEmployeeInDataStore(expectedChangedEmployee) } answers { simulateUpdate(firstArg()) }

            val result = updateEmployeeById(id, change)

            result shouldBe SuccessfullyUpdated(expectedUpdatedEmployee)
            verify { publishEvent(EmployeeUpdatedEvent(expectedUpdatedEmployee)) }
        }

        @Test
        fun `updating a non-existing employee returns employee not found result`() {
            every { getEmployeeById(id) } returns null

            val result = updateEmployeeById(employee.id) { it.copy(title = JobTitle("New-Title")) }

            result shouldBe EmployeeNotFound

            verify { updateEmployeeInDataStore wasNot called }
            verify { publishEvent wasNot called }
        }

        @Test
        fun `not actually changing anything returns employee not changed result`() {
            every { getEmployeeById(id) } returns employee

            val result = updateEmployeeById(employee.id) { it }

            result shouldBe EmployeeNotChanged(employee)
            verify { publishEvent wasNot called }
        }

        @TestFactory
        fun `certain modifications are prohibited`(): List<DynamicTest> = listOf(
            prohibitedModificationTest("Changing the ID") {
                it.copy(id = uuid())
            },
            prohibitedModificationTest("Changing the Version") {
                it.copy(version = 5)
            },
            prohibitedModificationTest("Changing the Last Update") {
                it.copy(lastUpdate = instant("2020-01-01T12:00:00.789Z"))
            }
        )

        private fun prohibitedModificationTest(name: String, operation: (Employee) -> (Employee)) =
            dynamicTest(name) {
                every { getEmployeeById(id) } returns employee
                assertThrows<IllegalStateException> {
                    updateEmployeeById(id, operation)
                }
                verify { updateEmployeeInDataStore wasNot called }
                verify { publishEvent wasNot called }
            }

        private fun simulateUpdate(it: Employee) =
            it.copy(version = it.version + 1, lastUpdate = it.lastUpdate.plusSeconds(10))

    }

    @Nested
    @ResetMocksAfterEachTest
    @TechnologyIntegrationTest
    @SpringBootTest(classes = [RetryTestsConfiguration::class])
    inner class RetryTests(
        @Autowired private val getEmployeeById: GetEmployeeByIdFunction,
        @Autowired private val updateEmployeeInDataStore: UpdateEmployeeInDataStoreFunction,
        @Autowired private val updateEmployeeById: UpdateEmployeeByIdFunction
    ) {

        @Test
        fun `operation is retried up to 5 times in case of concurrent update exceptions`() {
            every { getEmployeeById(id) } returns employee
            every { updateEmployeeInDataStore(any()) } throws ConcurrentEmployeeUpdateException()
            assertThrows<ConcurrentEmployeeUpdateException> {
                updateEmployeeById(id) { it.copy(title = JobTitle("New Title")) }
            }
            verify(exactly = 5) { getEmployeeById(id) }
        }

        @Test
        fun `operation does not fail if retrying fixes the problem`() {
            every { getEmployeeById(id) } returns employee
            every { updateEmployeeInDataStore(any()) }
                .throws(ConcurrentEmployeeUpdateException())
                .andThenThrows(ConcurrentEmployeeUpdateException())
                .andThen(employee)

            updateEmployeeById(id) { it.copy(title = JobTitle("New Title")) }

            verify(exactly = 3) { getEmployeeById(id) }
        }

    }

    @EnableRetry
    @Import(UpdateEmployeeByIdFunction::class)
    private class RetryTestsConfiguration {

        @Bean
        fun getEmployeeById(): GetEmployeeByIdFunction = mockk(relaxed = true)

        @Bean
        fun updateEmployeeInDataStore(): UpdateEmployeeInDataStoreFunction = mockk(relaxed = true)

        @Bean
        fun publishEvent(): PublishEventFunction = mockk(relaxed = true)

    }

}
