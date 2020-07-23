package skillmanagement.domain.employees.metrics

import io.micrometer.core.instrument.MeterRegistry
import mu.KotlinLogging.logger
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.Scheduled
import skillmanagement.domain.MetricProvider
import skillmanagement.domain.TechnicalFunction
import java.util.concurrent.atomic.AtomicLong
import javax.annotation.PostConstruct

private const val RATE_PROPERTY = "\${metrics.total-number-of-employees.update.rate}"
private const val DELAY_PROPERTY = "\${metrics.total-number-of-employees.update.delay}"

@MetricProvider
class TotalNumberOfEmployees(
    private val getTotalNumberOfEmployeesFromDataStore: GetTotalNumberOfEmployeesFromDataStore,
    private val registry: MeterRegistry
) {

    private val log = logger {}
    private val totalNumberOfEmployees = AtomicLong()

    @PostConstruct
    fun init() {
        registry.gauge("employees.total", totalNumberOfEmployees)
    }

    @Scheduled(fixedRateString = RATE_PROPERTY, initialDelayString = DELAY_PROPERTY)
    fun updateCache() {
        log.debug { "Updating TotalNumberOfEmployees cache" }
        val newTotal = getTotalNumberOfEmployeesFromDataStore()
        totalNumberOfEmployees.set(newTotal)
        log.debug { "TotalNumberOfEmployees cache updated: $newTotal" }
    }

}

@TechnicalFunction
class GetTotalNumberOfEmployeesFromDataStore(
    private val jdbcTemplate: JdbcTemplate
) {

    private val query = "SELECT count(*) AS total FROM employees"

    operator fun invoke(): Long = jdbcTemplate.queryForObject(query, Long::class.java)!!

}
