package skillmanagement.domain.projects.find

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.projects.Project
import skillmanagement.domain.projects.ProjectRowMapper

@TechnicalFunction
class FindProjectsInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    private val query = "SELECT * FROM projects"

    @Transactional(readOnly = true)
    operator fun invoke(): List<Project> {
        return jdbcTemplate.query(query, ProjectRowMapper)
    }

}
