package skillmanagement.domain.employees.usecases.update

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.domain.employees.model.employee_john_doe
import skillmanagement.domain.employees.model.employee_john_smith
import skillmanagement.domain.employees.usecases.create.InsertEmployeeIntoDataStoreFunction
import skillmanagement.domain.employees.usecases.read.GetEmployeesFromDataStoreFunction
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.fixedClock

@JdbcTest
@AutoConfigureJson
@TestInstance(PER_CLASS)
@TechnologyIntegrationTest
internal class UpdateEmployeeInDataStoreFunctionTests(
    @Autowired val jdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired val objectMapper: ObjectMapper
) {

    private val clock = fixedClock("2020-07-16T12:34:56.789Z")

    private val getEmployee = GetEmployeesFromDataStoreFunction(jdbcTemplate, objectMapper)
    private val insertEmployeeIntoDataStore = InsertEmployeeIntoDataStoreFunction(jdbcTemplate, objectMapper)
    private val updateEmployeeInDataStore = UpdateEmployeeInDataStoreFunction(jdbcTemplate, objectMapper, clock)

    @Test
    fun `updating an existing employee actually persists it`() {
        insertEmployeeIntoDataStore(employee_jane_doe)
        val updatedEmployee = updateEmployeeInDataStore(employee_jane_doe)
        val loadedEmployee = getEmployee(employee_jane_doe.id)
        assertThat(updatedEmployee).isEqualTo(loadedEmployee)
    }

    @Test
    fun `updating an existing employee increments it's version and set the last update timestamp`() {
        insertEmployeeIntoDataStore(employee_jane_doe)
        val updatedEmployee = updateEmployeeInDataStore(employee_jane_doe)
        assertThat(updatedEmployee).isNotEqualTo(employee_jane_doe)
        assertThat(updatedEmployee.version).isEqualTo(2)
        assertThat(updatedEmployee.lastUpdate).isEqualTo(clock.instant())
    }

    /**
     * An existence check before executing the update would prevent this,
     * but also add another round trip to the database...
     **/
    @Test
    fun `updating a non-existing employee fails with slightly wrong exception`() {
        assertThrows<ConcurrentEmployeeUpdateException> {
            updateEmployeeInDataStore(employee_john_doe)
        }
    }

    /**
     * An existence check before executing the update would prevent this,
     * but also add another round trip to the database...
     **/
    @Test
    fun `updating an existing employee based on data from an old version throws exception`() {
        insertEmployeeIntoDataStore(employee_john_smith.copy(version = 42))
        assertThrows<ConcurrentEmployeeUpdateException> {
            updateEmployeeInDataStore(employee_john_smith.copy(version = 41))
        }
    }

}
