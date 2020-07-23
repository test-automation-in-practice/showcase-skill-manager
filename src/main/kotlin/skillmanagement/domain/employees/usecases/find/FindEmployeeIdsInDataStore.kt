package skillmanagement.domain.employees.usecases.find

import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.stereotypes.TechnicalFunction
import java.util.UUID

@TechnicalFunction
class FindEmployeeIdsInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val allQuery = "SELECT id FROM employees"
    private val allQueryForSkill = "SELECT id FROM employees WHERE skill_ids LIKE :skillId"
    private val allQueryForProject = "SELECT id FROM employees WHERE project_ids LIKE :projectId"

    private val rowMapper: RowMapper<UUID> = RowMapper { rs, _ -> UUID.fromString(rs.getString("id")) }

    operator fun invoke(query: FindEmployeeQuery): List<UUID> {
        val (queryString, parameters) = when (query) {
            is NoOpQuery -> allQuery to emptyMap()
            is EmployeesWithSkill -> allQueryForSkill to mapOf("skillId" to "%${query.skillId}%")
            is EmployeesWhoWorkedOnProject -> allQueryForProject to mapOf("projectId" to "%${query.projectId}%")
        }
        return jdbcTemplate.query(queryString, parameters, rowMapper)
    }

}
