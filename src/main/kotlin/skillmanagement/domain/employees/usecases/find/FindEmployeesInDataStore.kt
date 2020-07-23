package skillmanagement.domain.employees.usecases.find

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.TechnicalFunction
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

    operator fun invoke(query: FindEmployeeQuery): List<Employee> {
        val (queryString, parameters) = when (query) {
            is NoOpQuery -> allQuery to emptyMap()
            is EmployeesWithSkill -> allQueryForSkill to mapOf("skillId" to "%${query.skillId}%")
            is EmployeesWhoWorkedOnProject -> allQueryForProject to mapOf("projectId" to "%${query.projectId}%")
        }
        return jdbcTemplate.query(queryString, parameters, rowMapper)
    }

}
