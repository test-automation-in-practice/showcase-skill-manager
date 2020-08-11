package skillmanagement.domain.employees.usecases.find

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.employees.model.Employee

@TechnicalFunction
class FindEmployeesInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val allQuery = "SELECT data FROM employees"
    private val allQueryForSkill = "SELECT data FROM employees WHERE skill_ids LIKE :skillId"
    private val allQueryForProject = "SELECT data FROM employees WHERE project_ids LIKE :projectId"

    private val rowMapper: RowMapper<Employee> = RowMapper { rs, _ ->
        objectMapper.readValue<Employee>(rs.getString("data"))
    }

    operator fun invoke(): List<Employee> =
        jdbcTemplate.query(allQuery, rowMapper)

    operator fun invoke(query: EmployeesWithSkill): List<Employee> =
        jdbcTemplate.query(allQueryForSkill, mapOf("skillId" to "%${query.skillId}%"), rowMapper)

    operator fun invoke(query: EmployeesWhoWorkedOnProject): List<Employee> =
        jdbcTemplate.query(allQueryForProject, mapOf("projectId" to "%${query.projectId}%"), rowMapper)

}
