package skillmanagement.domain.skills

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

data class Skill(
    val id: UUID,
    val label: SkillLabel
)

data class SkillLabel @JsonCreator constructor(
    @JsonValue private val value: String
) {
    override fun toString() = value
}
