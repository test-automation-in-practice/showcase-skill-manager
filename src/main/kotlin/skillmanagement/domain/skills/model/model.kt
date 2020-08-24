package skillmanagement.domain.skills.model

import skillmanagement.common.model.Label
import skillmanagement.common.model.StringType
import skillmanagement.common.validation.hasMaxLength
import skillmanagement.common.validation.matchesPattern
import java.time.Instant
import java.util.SortedSet
import java.util.UUID

// TODO: add description property

data class Skill(
    val id: UUID,
    val version: Int,
    val label: SkillLabel,
    val tags: SortedSet<Tag>,
    val lastUpdate: Instant
)

class SkillLabel(value: String) : Label(value, maxLength = 100)

private val TAG_PATTERN = Regex("""[a-z]+([-_][a-z]+)*""")

class Tag(value: String) :
    StringType(value, { hasMaxLength(50); matchesPattern(TAG_PATTERN) })
