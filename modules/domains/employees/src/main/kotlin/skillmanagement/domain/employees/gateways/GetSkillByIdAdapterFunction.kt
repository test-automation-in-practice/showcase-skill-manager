package skillmanagement.domain.employees.gateways

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.SkillData
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.usecases.read.GetSkillByIdFunction
import java.util.UUID

/**
 * This adapter function provides an abstraction for getting [SkillData] from
 * the _skills_ domain module.
 *
 * This is done in order to reduce the coupling between different domains as
 * much as possible. If this project were ever to be split into multiple
 * runtimes, this function would need to be changed to a network call
 * (e.g. HTTP or RSocket etc.).
 */
@BusinessFunction
internal class GetSkillByIdAdapterFunction(
    private val getSkillById: GetSkillByIdFunction
) {
    operator fun invoke(id: UUID): SkillData? = getSkillById(id)?.toData()
    private fun Skill.toData() = SkillData(id = id, label = label.toString())
}