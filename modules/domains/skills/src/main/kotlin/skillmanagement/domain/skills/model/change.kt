package skillmanagement.domain.skills.model

import java.util.SortedSet

internal data class SkillChangeData(
    val label: SkillLabel,
    val description: SkillDescription?,
    val tags: SortedSet<Tag>
)

internal fun Skill.toChangeData(): SkillChangeData =
    SkillChangeData(label = label, description = description, tags = tags)

internal fun Skill.merge(changes: SkillChangeData): Skill =
    copy(label = changes.label, description = changes.description, tags = changes.tags)
