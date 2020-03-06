package skillmanagement.domain.projects.add

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.annotation.Transactional
import skillmanagement.common.insert
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.projects.Project

@TechnicalFunction
class InsertProjectIntoDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    @Transactional
    operator fun invoke(project: Project) = jdbcTemplate.insert(
        table = "projects",
        columnValues = listOf(
            "id" to "${project.id}",
            "label" to "${project.label}",
            "description" to "${project.description}"
        )
    )

}
