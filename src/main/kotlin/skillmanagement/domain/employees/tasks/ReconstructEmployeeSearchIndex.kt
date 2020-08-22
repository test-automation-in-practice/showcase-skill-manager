package skillmanagement.domain.employees.tasks

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging.logger
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Component
import skillmanagement.common.stereotypes.Task
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.searchindex.EmployeeSearchIndex
import kotlin.system.measureTimeMillis

@Component
@WebEndpoint(id = "reconstructEmployeeSearchIndex")
class ReconstructEmployeeSearchIndexEndpoint(
    private val reconstructEmployeeSearchIndex: ReconstructEmployeeSearchIndex
) {

    @WriteOperation
    fun execute() {
        reconstructEmployeeSearchIndex.run()
    }

}

@Task
class ReconstructEmployeeSearchIndex(
    private val findEmployeesInDataStore: FindEmployeesInDataStore,
    private val searchIndex: EmployeeSearchIndex
) : Runnable {

    private val log = logger {}

    override fun run() {
        log.info { "Reconstructing employees index ..." }
        val resetDuration = resetIndex()
        val indexingDuration = indexAllKnownSkills()
        log.info { "Reconstruction of employees index succeeded. Took ${resetDuration + indexingDuration}ms." }
    }

    private fun resetIndex(): Long {
        log.debug { "Resetting employees index ..." }
        val resetDuration = measureTimeMillis {
            searchIndex.reset()
        }
        log.debug { "Employees index successfully reset." }
        return resetDuration
    }

    private fun indexAllKnownSkills(): Long {
        log.debug { "Indexing all known employees ..." }
        val indexingDuration = measureTimeMillis {
            findEmployeesInDataStore { employee ->
                log.debug { "Indexing [${employee.id} - ${employee.firstName} ${employee.lastName}]" }
                searchIndex.index(employee)
            }
        }
        log.debug { "All known employees successfully indexed." }
        return indexingDuration
    }

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
