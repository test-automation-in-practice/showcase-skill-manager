package skillmanagement.domain.employees.usecases.create

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.employees.model.EmployeeEntity

@TechnicalFunction
internal class InsertEmployeeIntoDataStoreFunction(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val statement = """
        INSERT INTO employees (id, version, data, created_utc, last_update_utc)
        VALUES (:id, :version, :data, :created, :lastUpdate)
        """

    operator fun invoke(employee: EmployeeEntity) {
        val parameters = with(employee) {
            mapOf(
                "id" to "$id",
                "version" to version,
                "data" to objectMapper.writeValueAsString(data),
                "created" to "$created",
                "lastUpdate" to "$lastUpdate"
            )
        }
        jdbcTemplate.update(statement, parameters)
    }

}
