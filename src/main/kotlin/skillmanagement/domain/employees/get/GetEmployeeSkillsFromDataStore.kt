package skillmanagement.domain.employees.get

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.SkillKnowledge
import skillmanagement.domain.employees.SkillKnowledgeRowMapper
import java.util.UUID

@TechnicalFunction
class GetEmployeeSkillsFromDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val employeeSkillsQuery = """
        SELECT es.*, s.label
        FROM employee_skills es
        LEFT OUTER JOIN skills s
          ON es.skill_id = s.id
        WHERE es.employee_id = :id
        """.trimIndent()

    @Transactional(readOnly = true)
    operator fun invoke(id: UUID): List<SkillKnowledge> =
        jdbcTemplate.query(employeeSkillsQuery, mapOf("id" to "$id"), SkillKnowledgeRowMapper)

}
