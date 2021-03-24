@file:Suppress("MatchingDeclarationName")

package skillmanagement.domain.skills.model

import java.util.Collections.emptySortedSet
import java.util.SortedSet

data class SkillCreationData(
    val label: SkillLabel,
    val description: SkillDescription? = null,
    val tags: SortedSet<Tag> = emptySortedSet()
)
