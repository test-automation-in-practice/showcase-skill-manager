package skillmanagement.domain.employees.metrics

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
import skillmanagement.domain.employees.model.employee_john_smith
import skillmanagement.domain.employees.usecases.create.InsertEmployeeIntoDataStoreFunction
import skillmanagement.domain.employees.usecases.delete.DeleteEmployeeFromDataStoreFunction
import skillmanagement.test.TechnologyIntegrationTest

@JdbcTest
@AutoConfigureJson
@TestInstance(PER_CLASS)
@TechnologyIntegrationTest
internal class GetTotalNumberOfEmployeesFromDataStoreFunctionTests(
    @Autowired jdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired objectMapper: ObjectMapper
) {

    private val delete = DeleteEmployeeFromDataStoreFunction(jdbcTemplate)
    private val insert = InsertEmployeeIntoDataStoreFunction(jdbcTemplate, objectMapper)
    private val getTotalNumberOfEmployees = GetTotalNumberOfEmployeesFromDataStoreFunction(jdbcTemplate.jdbcTemplate)

    @Test
    fun `returns the total number of employee in the data store`() {
        getTotalNumberOfEmployees() shouldBe 0

        insert(employee_jane_doe)
        getTotalNumberOfEmployees() shouldBe 1

        insert(employee_john_smith)
        getTotalNumberOfEmployees() shouldBe 2

        delete(employee_john_smith.id)
        getTotalNumberOfEmployees() shouldBe 1
    }

}
