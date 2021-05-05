package skillmanagement.runtime.bridges

import skillmanagement.domain.employees.model.ProjectData
import skillmanagement.domain.employees.model.ProjectId
import skillmanagement.domain.employees.model.SkillData
import skillmanagement.domain.employees.model.SkillId
import skillmanagement.domain.employees.model.projectId
import skillmanagement.domain.employees.model.skillId
import skillmanagement.test.uuid

internal val project_data_neo = ProjectData(
    id = projectId("f804d83f-466c-4eab-a58f-4b25ca1778f3"),
    label = "Neo",
    description = "The PlayStation 4 Pro."
)

internal val skill_data_kotlin = SkillData(
    id = skillId("3f7985b9-f5f0-4662-bda9-1dcde01f5f3b"),
    label = "Kotlin"
)

fun externalProjectId() = ProjectId(uuid())
fun externalSkillId() = SkillId(uuid())
