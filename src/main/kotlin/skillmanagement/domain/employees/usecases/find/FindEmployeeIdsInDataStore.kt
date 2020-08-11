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

    operator fun invoke(): List<UUID> =
        jdbcTemplate.query(allQuery, rowMapper)

    operator fun invoke(query: EmployeesWithSkill): List<UUID> =
        jdbcTemplate.query(allQueryForSkill, mapOf("skillId" to "%${query.skillId}%"), rowMapper)

    operator fun invoke(query: EmployeesWhoWorkedOnProject): List<UUID> =
        jdbcTemplate.query(allQueryForProject, mapOf("projectId" to "%${query.projectId}%"), rowMapper)

}
