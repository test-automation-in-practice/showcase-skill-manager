package skillmanagement.domain.employees.update

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.employees.Employee
import java.time.Clock

@TechnicalFunction
class UpdateEmployeeInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper,
    private val clock: Clock
) {

    private val statement = """
        UPDATE employees
        SET version = :version, data = :data, skill_ids = :skillIds, project_ids = :projectIds
        WHERE id = :id AND version = :expectedVersion
        """

    @Throws(ConcurrentEmployeeUpdateException::class)
    operator fun invoke(employee: Employee) = doUpdateEmployee(
        employee = employee.copy(version = employee.version + 1, lastUpdate = clock.instant()),
        expectedVersion = employee.version
    )

    private fun doUpdateEmployee(employee: Employee, expectedVersion: Int) {
        val parameters = mapOf(
            "id" to employee.id.toString(),
            "version" to employee.version,
            "data" to objectMapper.writeValueAsString(employee),
            "skillIds" to employee.skills.joinToString { it.skill.id.toString() },
            "projectIds" to employee.projects.joinToString { it.project.id.toString() },
            "expectedVersion" to expectedVersion
        )
        if (jdbcTemplate.update(statement, parameters) == 0) throw ConcurrentEmployeeUpdateException()
    }

}

class ConcurrentEmployeeUpdateException : RuntimeException()
