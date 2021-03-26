package skillmanagement.domain.employees.usecases.create

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson
import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.domain.employees.model.employee_john_smith
import skillmanagement.domain.employees.usecases.read.GetEmployeesFromDataStoreFunction
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.uuid

@JdbcTest
@AutoConfigureJson
@TestInstance(PER_CLASS)
@TechnologyIntegrationTest
internal class InsertEmployeeIntoDataStoreFunctionTests(
    @Autowired val jdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired val objectMapper: ObjectMapper
) {

    private val getEmployee = GetEmployeesFromDataStoreFunction(jdbcTemplate, objectMapper)
    private val insertEmployeeIntoDataStore = InsertEmployeeIntoDataStoreFunction(jdbcTemplate, objectMapper)

    @Test
    fun `inserts complete employee data into data store`() {
        val employee = employee_jane_doe.copy(id = uuid())

        getEmployee(employee.id) shouldBe null
        insertEmployeeIntoDataStore(employee)
        getEmployee(employee.id) shouldBe employee
    }

    @Test
    fun `fails when trying to insert employee with existing ID`() {
        val id = uuid()

        val employee1 = employee_jane_doe.copy(id = id)
        val employee2 = employee_john_smith.copy(id = id)

        insertEmployeeIntoDataStore(employee1)
        assertThrows<DuplicateKeyException> {
            insertEmployeeIntoDataStore(employee2)
        }
    }

}
