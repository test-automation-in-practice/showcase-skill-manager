package skillmanagement.domain.employees.find

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.ProjectAssignment
import skillmanagement.domain.employees.ProjectAssignmentRowMapper
import java.util.UUID

@TechnicalFunction
class FindEmployeeProjectsInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val employeesProjectsQuery = """
        SELECT epa.*, p.label, p.description
        FROM employee_project_assignments epa
        LEFT OUTER JOIN projects p
          ON epa.project_id = p.id
        WHERE epa.employee_id IN (:ids)
        """.trimIndent()

    @Transactional(readOnly = true)
    operator fun invoke(employeeIds: Collection<UUID>): Map<UUID, List<ProjectAssignment>> {
        if (employeeIds.isEmpty()) {
            return emptyMap()
        }

        val map = mutableMapOf<UUID, MutableList<ProjectAssignment>>()
        jdbcTemplate.query(employeesProjectsQuery, mapOf("ids" to employeeIds.map { "$it" })) { rs ->
            val employeeId = rs.getString("employee_id").let { UUID.fromString(it) }
            val projectAssignment = ProjectAssignmentRowMapper.mapRow(rs, -1)

            map.computeIfAbsent(employeeId) { mutableListOf() }.add(projectAssignment)
        }
        return map
    }

}
