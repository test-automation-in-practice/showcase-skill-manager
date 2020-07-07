package skillmanagement.domain.employees.find

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee

@TechnicalFunction
class FindEmployeesInDataStore(
    private val jdbcTemplate: JdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val rowMapper: RowMapper<Employee> = RowMapper { rs, _ ->
        objectMapper.readValue<Employee>(rs.getString("data"))
    }

    operator fun invoke(query: FindEmployeeQuery): List<Employee> {
        val queryString = when (query) {
            is NoOpQuery -> "SELECT data FROM employees"
            is EmployeesWithSkill -> "SELECT data FROM employees WHERE skill_ids LIKE '%${query.skillId}%'"
            is EmployeesWhoWorkedOnProject -> "SELECT data FROM employees WHERE project_ids LIKE '%${query.projectId}%'"
        }
        return jdbcTemplate.query(queryString, rowMapper)
    }

}
