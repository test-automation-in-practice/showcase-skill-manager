package skillmanagement.domain.skills.usecases.create

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.stereotypes.TechnicalFunction
import skillmanagement.domain.skills.model.SkillEntity

@TechnicalFunction
internal class InsertSkillIntoDataStoreFunction(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val statement = """
        INSERT INTO skills (id, version, data, created_utc, last_update_utc)
        VALUES (:id, :version, :data, :created, :lastUpdate)
        """

    operator fun invoke(skill: SkillEntity) {
        val parameters = with(skill) {
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
