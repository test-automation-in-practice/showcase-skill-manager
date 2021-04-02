@file:Suppress("MatchingDeclarationName")

package skillmanagement.domain.skills.model

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class StringToSkillIdConverter : Converter<String, SkillId> {
    override fun convert(source: String): SkillId = skillId(source)
}
