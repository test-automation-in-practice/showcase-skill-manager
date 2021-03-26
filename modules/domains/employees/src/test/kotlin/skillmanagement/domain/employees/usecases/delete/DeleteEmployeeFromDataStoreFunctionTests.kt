package skillmanagement.domain.employees.usecases.delete

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.domain.employees.usecases.create.InsertEmployeeIntoDataStoreFunction
import skillmanagement.domain.employees.usecases.read.GetEmployeesFromDataStoreFunction
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.uuid

@JdbcTest
@AutoConfigureJson
@TestInstance(PER_CLASS)
@TechnologyIntegrationTest
internal class DeleteEmployeeFromDataStoreFunctionTests(
    @Autowired val jdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired val objectMapper: ObjectMapper
) {

    private val getEmployee = GetEmployeesFromDataStoreFunction(jdbcTemplate, objectMapper)
    private val insertEmployeeIntoDataStore = InsertEmployeeIntoDataStoreFunction(jdbcTemplate, objectMapper)

    private val deleteEmployeeFromDataStore = DeleteEmployeeFromDataStoreFunction(jdbcTemplate)

    @Test
    fun `does not fail if employee with id does not exist`() {
        val id = uuid()
        getEmployee(id) shouldBe null
        deleteEmployeeFromDataStore(id)
    }

    @Test
    fun `deletes existing employee from data store`() {
        val employee = employee_jane_doe
        val id = employee.id

        getEmployee(id) shouldBe null
        insertEmployeeIntoDataStore(employee)
        getEmployee(id) shouldBe employee
        deleteEmployeeFromDataStore(id)
        getEmployee(id) shouldBe null
    }

}
