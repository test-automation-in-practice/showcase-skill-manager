package skillmanagement.domain.skills.metrics

import io.micrometer.core.instrument.MeterRegistry
import mu.KotlinLogging.logger
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.Scheduled
import skillmanagement.common.stereotypes.MetricProvider
import skillmanagement.common.stereotypes.TechnicalFunction
import java.util.concurrent.atomic.AtomicLong
import javax.annotation.PostConstruct

private const val RATE_PROPERTY = "\${metrics.total-number-of-skills.update.rate}"
private const val DELAY_PROPERTY = "\${metrics.total-number-of-skills.update.delay}"

@MetricProvider
class TotalNumberOfSkills(
    private val getTotalNumberOfSkillsFromDataStore: GetTotalNumberOfSkillsFromDataStore,
    private val registry: MeterRegistry
) {

    private val log = logger {}
    private val totalNumberOfSkills = AtomicLong()

    @PostConstruct
    fun init() {
        registry.gauge("skills.total", totalNumberOfSkills)
    }

    @Scheduled(fixedRateString = RATE_PROPERTY, initialDelayString = DELAY_PROPERTY)
    fun updateCache() {
        log.debug { "Updating TotalNumberOfSkills cache" }
        val newTotal = getTotalNumberOfSkillsFromDataStore()
        totalNumberOfSkills.set(newTotal)
        log.debug { "TotalNumberOfSkills cache updated: $newTotal" }
    }

}

@TechnicalFunction
class GetTotalNumberOfSkillsFromDataStore(
    private val jdbcTemplate: JdbcTemplate
) {

    private val query = "SELECT count(*) AS total FROM skills"

    operator fun invoke(): Long = jdbcTemplate.queryForObject(query, Long::class.java)!!

}
