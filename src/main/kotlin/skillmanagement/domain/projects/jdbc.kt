package skillmanagement.domain.projects

import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import java.util.UUID

object ProjectRowMapper : RowMapper<Project> {

    override fun mapRow(rs: ResultSet, rowNum: Int) =
        Project(id = rs.id, label = rs.label, description = rs.description)

    private val ResultSet.id: UUID
        get() = UUID.fromString(getString("id"))
    private val ResultSet.label: ProjectLabel
        get() = ProjectLabel(getString("label"))
    private val ResultSet.description: ProjectDescription
        get() = ProjectDescription(getString("description"))

}
