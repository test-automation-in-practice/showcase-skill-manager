package skillmanagement.domain.projects.find

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.projects.Project

@TechnicalFunction
class FindProjectsInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {

    private val query = "SELECT data FROM projects"

    private val rowMapper: RowMapper<Project> = RowMapper { rs, _ ->
        objectMapper.readValue<Project>(rs.getString("data"))
    }

    operator fun invoke(): List<Project> {
        return jdbcTemplate.query(query, emptyMap<String, Any>(), rowMapper)
    }

}
