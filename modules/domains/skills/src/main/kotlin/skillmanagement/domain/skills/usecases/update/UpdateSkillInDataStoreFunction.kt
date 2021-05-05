package skillmanagement.domain.skills.usecases.update

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.skills.model.SkillEntity
import java.time.Clock

@TechnicalFunction
internal class UpdateSkillInDataStoreFunction(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper,
    private val clock: Clock
) {

    private val statement = """
        UPDATE skills
        SET version = :version,
            data = :data,
            last_update_utc = :lastUpdate
        WHERE id = :id
          AND version = :expectedVersion
        """

    /**
     * Updates the given [SkillEntity] in the data store.
     *
     * This also updates the [SkillEntity.version] and [SkillEntity.lastUpdate]
     * properties. Always use the returned [SkillEntity] instance for subsequent
     * operation!
     *
     * @throws ConcurrentSkillUpdateException in case there was a concurrent
     * update to this skill's data while the invoking operation was working.
     */
    operator fun invoke(skill: SkillEntity): SkillEntity = doUpdateSkill(
        skill = skill.copy(version = skill.version + 1, lastUpdate = clock.instant()),
        expectedVersion = skill.version
    )

    private fun doUpdateSkill(skill: SkillEntity, expectedVersion: Int): SkillEntity {
        val parameters = with(skill) {
            mapOf(
                "id" to "$id",
                "version" to version,
                "data" to objectMapper.writeValueAsString(data),
                "lastUpdate" to "$lastUpdate",
                "expectedVersion" to expectedVersion
            )
        }
        if (jdbcTemplate.update(statement, parameters) == 0) throw ConcurrentSkillUpdateException()
        return skill
    }

}

internal class ConcurrentSkillUpdateException : RuntimeException()
