@file:Suppress("MatchingDeclarationName")

package skillmanagement.domain.projects.model

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class StringToProjectIdConverter : Converter<String, ProjectId> {
    override fun convert(source: String): ProjectId = projectId(source)
}
