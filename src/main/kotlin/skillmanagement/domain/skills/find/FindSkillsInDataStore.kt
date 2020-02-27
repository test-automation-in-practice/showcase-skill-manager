package skillmanagement.domain.skills.find

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.skills.Skill

@TechnicalFunction
class FindSkillsInDataStore(
    private val jdbcTemplate: NamedParameterJdbcTemplate
) {

    operator fun invoke(pageNumber: Int, pageSize: Int): Page<Skill> {
        TODO()
    }

}

// TODO
interface Page<T> {
    val content: Collection<T>
    val size: Int
    val number: Int
    val totalElements: Long
    val totalPages: Int
    val isFirst: Boolean
    val isLast: Boolean
}
