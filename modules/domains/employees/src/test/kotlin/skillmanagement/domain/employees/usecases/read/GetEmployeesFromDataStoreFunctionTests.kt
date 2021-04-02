package skillmanagement.domain.employees.usecases.read

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.shouldBe
import io.mockk.called
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.testit.testutils.logrecorder.api.LogRecord
import org.testit.testutils.logrecorder.junit5.RecordLoggers
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.EmployeeId
import skillmanagement.domain.employees.model.employeeId
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.domain.employees.model.employee_john_smith
import skillmanagement.domain.employees.usecases.create.InsertEmployeeIntoDataStoreFunction
import skillmanagement.domain.employees.usecases.delete.DeleteEmployeeFromDataStoreFunction
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.TechnologyIntegrationTest

@JdbcTest
@AutoConfigureJson
@TestInstance(PER_CLASS)
@TechnologyIntegrationTest
internal class GetEmployeesFromDataStoreFunctionTests(
    @Autowired val jdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired val objectMapper: ObjectMapper
) {

    private val insertEmployeeIntoDataStore = InsertEmployeeIntoDataStoreFunction(jdbcTemplate, objectMapper)
    private val deleteEmployeeFromDataStore = DeleteEmployeeFromDataStoreFunction(jdbcTemplate)
    private val getEmployeesFromDataStore = GetEmployeesFromDataStoreFunction(jdbcTemplate, objectMapper)

    @AfterEach
    fun deleteEmployees() {
        deleteEmployeeFromDataStore()
    }

    @Nested
    inner class SingleId {

        @Test
        fun `returns NULL if nothing found with given ID`() {
            getSingleEmployee(employeeId()) shouldBe null
        }

        @Test
        fun `returns Employee if found by its ID`() {
            insert(employee_jane_doe)
            getSingleEmployee(employee_jane_doe.id) shouldBe employee_jane_doe
        }

        private fun getSingleEmployee(id: EmployeeId) = getEmployeesFromDataStore(id)

    }

    @Nested
    inner class MultipleIds {

        @Test
        fun `returns empty Map for empty ID list`() {
            getMultipleEmployees() shouldBe emptyMap()
        }

        @Test
        fun `returns empty map if none of the Employees were found`() {
            getMultipleEmployees(employeeId()) shouldBe emptyMap()
        }

        @Test
        fun `returns map with every found Employee`() {
            insert(employee_jane_doe, employee_john_smith)

            val actualEmployees = getMultipleEmployees(employee_jane_doe.id, employeeId(), employee_john_smith.id)
            val expectedEmployees = setOf(employee_jane_doe, employee_john_smith).map { it.id to it }.toMap()

            actualEmployees shouldBe expectedEmployees
        }

        private fun getMultipleEmployees(vararg ids: EmployeeId) = getEmployeesFromDataStore(ids.toList(), chunkSize = 2)

    }

    @Nested
    @ResetMocksAfterEachTest
    inner class AllWithCallback {

        private val callback: (Employee) -> Unit = mockk(relaxed = true)

        @Test
        fun `callback is never invoked if there are no employees`() {
            execute()
            verify { callback wasNot called }
        }

        @Test
        fun `callback is invoked for each existing employee`() {
            insert(employee_jane_doe, employee_john_smith)

            execute()

            verify {
                callback(employee_jane_doe)
                callback(employee_john_smith)
            }
            confirmVerified(callback)
        }

        private fun execute() = getEmployeesFromDataStore(callback)

    }

    @Nested
    inner class Deserialization {

        private val employee = employee_jane_doe
        private val employeeId = employee.id

        @Test
        @RecordLoggers(EmployeeRowMapper::class)
        fun `deserialization errors are logged but don't throw an exception`(log: LogRecord) {
            insert(employee)

            assertThat(getEmployeesFromDataStore(employeeId)).isNotNull()
            corruptData(employeeId)
            assertThat(getEmployeesFromDataStore(employeeId)).isNull()

            val messages = log.messages
            assertThat(messages).hasSize(2)
            assertThat(messages[0]).startsWith("Could not read data of employee [$employeeId]: Instantiation of")
            assertThat(messages[1]).startsWith("Corrupted data: {}")
        }

        private fun corruptData(employeeId: EmployeeId) {
            jdbcTemplate.update("UPDATE employees SET data = '{}' WHERE id = :id", mapOf("id" to "$employeeId"))
        }

    }

    private fun insert(vararg employees: Employee) = employees.forEach { insertEmployeeIntoDataStore(it) }

}
