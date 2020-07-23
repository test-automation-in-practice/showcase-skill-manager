package skillmanagement.domain.projects.metrics

import io.micrometer.core.instrument.MeterRegistry
import mu.KotlinLogging.logger
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.Scheduled
import skillmanagement.domain.MetricProvider
import skillmanagement.domain.TechnicalFunction
import java.util.concurrent.atomic.AtomicLong
import javax.annotation.PostConstruct

private const val RATE_PROPERTY = "\${metrics.total-number-of-projects.update.rate}"
private const val DELAY_PROPERTY = "\${metrics.total-number-of-projects.update.delay}"

@MetricProvider
class TotalNumberOfProjects(
    private val getTotalNumberOfProjectsFromDataStore: GetTotalNumberOfProjectsFromDataStore,
    private val registry: MeterRegistry
) {

    private val log = logger {}
    private val totalNumberOfProjects = AtomicLong()

    @PostConstruct
    fun init() {
        registry.gauge("projects.total", totalNumberOfProjects)
    }

    @Scheduled(fixedRateString = RATE_PROPERTY, initialDelayString = DELAY_PROPERTY)
    fun updateCache() {
        log.debug { "Updating TotalNumberOfProjects cache" }
        val newTotal = getTotalNumberOfProjectsFromDataStore()
        totalNumberOfProjects.set(newTotal)
        log.debug { "TotalNumberOfProjects cache updated: $newTotal" }
    }

}

@TechnicalFunction
class GetTotalNumberOfProjectsFromDataStore(
    private val jdbcTemplate: JdbcTemplate
) {

    private val query = "SELECT count(*) AS total FROM projects"

    operator fun invoke(): Long = jdbcTemplate.queryForObject(query, Long::class.java)!!

}
