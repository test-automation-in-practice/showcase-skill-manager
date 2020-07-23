package skillmanagement.domain.skills.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import skillmanagement.domain.Validation.Companion.validate
import skillmanagement.domain.hasMaxLengthOf
import skillmanagement.domain.isNotBlank
import skillmanagement.domain.matchesPattern
import java.time.Instant
import java.util.SortedSet
import java.util.UUID

data class Skill(
    val id: UUID,
    val version: Int,
    val label: SkillLabel,
    val tags: SortedSet<Tag>,
    val lastUpdate: Instant
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

private val TAG_PATTERN = Regex("""[a-z]+([-_][a-z]+)*""")

data class Tag @JsonCreator constructor(
    @JsonValue private val value: String
) : Comparable<Tag> {

    init {
        validate(value, "Tag") {
            matchesPattern(TAG_PATTERN)
            hasMaxLengthOf(50)
        }
    }

    override fun compareTo(other: Tag): Int = value.compareTo(other.value)
    override fun toString() = value

}
