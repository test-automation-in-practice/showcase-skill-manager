package skillmanagement.domain.employees.get

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.ProjectAssignment
import skillmanagement.domain.employees.ProjectAssignmentRowMapper
import java.util.UUID

@TechnicalFunction
class GetEmployeeProjectsFromDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val employeeProjectsQuery = """
        SELECT epa.*, p.label, p.description
        FROM employee_project_assignments epa
        LEFT OUTER JOIN projects p
          ON epa.project_id = p.id
        WHERE epa.employee_id = :id
        """.trimIndent()

    @Transactional(readOnly = true)
    operator fun invoke(id: UUID): List<ProjectAssignment> =
        jdbcTemplate.query(employeeProjectsQuery, mapOf("id" to "$id"), ProjectAssignmentRowMapper)

}
