package skillmanagement.domain.employees.add

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee

@TechnicalFunction
class InsertEmployeeIntoDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val statement = """
        INSERT INTO employees (id, data, skill_ids, project_ids)
        VALUES (:id, :data, :skillIds, :projectIds)
        """

    operator fun invoke(employee: Employee) {
        val parameters = mapOf(
            "id" to employee.id.toString(),
            "data" to objectMapper.writeValueAsString(employee),
            "skillIds" to employee.skills.joinToString { it.skill.id.toString() },
            "projectIds" to employee.projects.joinToString { it.project.id.toString() }
        )
        jdbcTemplate.update(statement, parameters)
    }

}
