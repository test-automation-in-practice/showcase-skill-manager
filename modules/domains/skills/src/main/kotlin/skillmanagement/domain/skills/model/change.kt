@file:Suppress("MatchingDeclarationName")

package skillmanagement.domain.skills.model

import java.util.SortedSet

internal data class SkillChangeData(
    val label: SkillLabel,
    val description: SkillDescription?,
    val tags: SortedSet<Tag>
)

internal fun SkillEntity.toChangeData(): SkillChangeData =
    SkillChangeData(label = label, description = description, tags = tags)

internal fun SkillEntity.merge(changes: SkillChangeData): SkillEntity =
    copy(label = changes.label, description = changes.description, tags = changes.tags)
