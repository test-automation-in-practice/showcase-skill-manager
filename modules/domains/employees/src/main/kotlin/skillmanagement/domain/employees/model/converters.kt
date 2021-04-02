package skillmanagement.domain.employees.model

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class StringToEmployeeIdConverter : Converter<String, EmployeeId> {
    override fun convert(source: String) = employeeId(source)
}

@Component
class StringToProjectAssignmentIdConverter : Converter<String, ProjectAssignmentId> {
    override fun convert(source: String) = projectAssignmentId(source)
}

@Component
class StringToExternalProjectIdConverter : Converter<String, ProjectId> {
    override fun convert(source: String) = projectId(source)
}

@Component
class StringToExternalSkillIdConverter : Converter<String, SkillId> {
    override fun convert(source: String) = skillId(source)
}
