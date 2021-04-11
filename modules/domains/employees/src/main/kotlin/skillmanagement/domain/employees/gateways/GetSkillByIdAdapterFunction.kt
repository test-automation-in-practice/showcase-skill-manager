package skillmanagement.domain.employees.gateways

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.SkillData
import skillmanagement.domain.employees.model.SkillId
import skillmanagement.domain.skills.model.SkillEntity
import skillmanagement.domain.skills.usecases.read.GetSkillByIdFunction
import skillmanagement.domain.skills.model.SkillId as ExternalSkillId

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

    operator fun invoke(id: SkillId): SkillData? =
        getSkillById(skillId(id))?.toData()

    private fun SkillEntity.toData() = SkillData(
        id = skillId(id),
        label = label.toString()
    )

}

internal fun skillId(id: SkillId) = ExternalSkillId(id.toUUID())
internal fun skillId(id: ExternalSkillId) = SkillId(id.toUUID())
