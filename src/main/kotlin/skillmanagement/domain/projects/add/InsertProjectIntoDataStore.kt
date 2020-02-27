package skillmanagement.domain.projects.add

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.common.insert
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.projects.Project

@TechnicalFunction
class InsertProjectIntoDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    operator fun invoke(project: Project) = jdbcTemplate.insert(
        tableName = "projects",
        columnValueMapping = listOf(
            "id" to "${project.id}",
            "label" to "${project.label}",
            "description" to "${project.description}"
        )
    )

}
