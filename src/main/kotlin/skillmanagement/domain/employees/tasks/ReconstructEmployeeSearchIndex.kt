package skillmanagement.domain.employees.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging.logger
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.search.AbstractReconstructSearchIndexTask
import skillmanagement.common.stereotypes.Task
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.searchindex.EmployeeSearchIndex

@Task
@WebEndpoint(id = "reconstructEmployeeSearchIndex")
class ReconstructEmployeeSearchIndex(
    override val searchIndex: EmployeeSearchIndex,
    private val findEmployeesInDataStore: FindEmployeesInDataStore
) : AbstractReconstructSearchIndexTask<Employee>() {

    override val log = logger {}

    override fun executeForAllInstancesInDataStore(callback: (Employee) -> Unit) = findEmployeesInDataStore(callback)
    override fun shortDescription(instance: Employee) = "${instance.id} - ${instance.firstName} ${instance.lastName}"

}

@TechnicalFunction
class FindEmployeesInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val query = "SELECT data FROM employees"

    operator fun invoke(callback: (Employee) -> Unit) =
        jdbcTemplate.query(query) { rs ->
            callback(objectMapper.readValue(rs.getString("data")))
        }

}
