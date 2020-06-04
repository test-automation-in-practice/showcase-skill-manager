package skillmanagement.domain.employees.find

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.SkillKnowledge
import skillmanagement.domain.employees.SkillKnowledgeRowMapper
import java.util.UUID

@TechnicalFunction
class FindEmployeeSkillsInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val employeesSkillsQuery = """
        SELECT es.*, s.label
        FROM employee_skills es
        LEFT OUTER JOIN skills s
          ON es.skill_id = s.id
        WHERE es.employee_id IN (:ids)
        """.trimIndent()

    @Transactional(readOnly = true)
    operator fun invoke(employeeIds: Collection<UUID>): Map<UUID, List<SkillKnowledge>> {
        if (employeeIds.isEmpty()) {
            return emptyMap()
        }

        val map = mutableMapOf<UUID, MutableList<SkillKnowledge>>()
        jdbcTemplate.query(employeesSkillsQuery, mapOf("ids" to employeeIds.map { "$it" })) { rs ->
            val employeeId = rs.getString("employee_id").let { UUID.fromString(it) }
            val skillKnowledge = SkillKnowledgeRowMapper.mapRow(rs, -1)

            map.computeIfAbsent(employeeId) { mutableListOf() }.add(skillKnowledge)
        }
        return map
    }

}
