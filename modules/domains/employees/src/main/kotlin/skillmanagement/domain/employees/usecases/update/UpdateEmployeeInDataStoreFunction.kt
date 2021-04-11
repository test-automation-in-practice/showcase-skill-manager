package skillmanagement.domain.employees.usecases.update

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.employees.model.EmployeeEntity
import java.time.Clock

@TechnicalFunction
internal class UpdateEmployeeInDataStoreFunction(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper,
    private val clock: Clock
) {

    private val statement = """
        UPDATE employees
        SET version = :version, data = :data
        WHERE id = :id AND version = :expectedVersion
        """

    /**
     * Updates the given [EmployeeEntity] in the data store.
     *
     * This also updates the [EmployeeEntity.version] and [EmployeeEntity.lastUpdate]
     * properties. Always use the returned [EmployeeEntity] instance for subsequent
     * operation!
     *
     * @throws ConcurrentEmployeeUpdateException in case there was a concurrent
     * update to this employee's data while the invoking operation was working.
     */
    operator fun invoke(employee: EmployeeEntity): EmployeeEntity = doUpdateEmployee(
        employee = employee.copy(version = employee.version + 1, lastUpdate = clock.instant()),
        expectedVersion = employee.version
    )

    private fun doUpdateEmployee(employee: EmployeeEntity, expectedVersion: Int): EmployeeEntity {
        val parameters = mapOf(
            "id" to employee.id.toString(),
            "version" to employee.version,
            "data" to objectMapper.writeValueAsString(employee),
            "expectedVersion" to expectedVersion
        )
        if (jdbcTemplate.update(statement, parameters) == 0) throw ConcurrentEmployeeUpdateException()
        return employee
    }

}

class ConcurrentEmployeeUpdateException : RuntimeException()
