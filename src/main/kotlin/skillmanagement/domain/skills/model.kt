package skillmanagement.domain.skills

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import skillmanagement.domain.Validation.Companion.validate
import skillmanagement.domain.hasMaxLengthOf
import skillmanagement.domain.isNotBlank
import java.util.UUID

data class Skill(
    val id: UUID,
    val label: SkillLabel
)

data class SkillLabel @JsonCreator constructor(
    @JsonValue private val value: String
) {

    init {
        validate(value, "Skill Label") {
            isNotBlank()
            hasMaxLengthOf(75)
        }
    }

    override fun toString() = value
}
