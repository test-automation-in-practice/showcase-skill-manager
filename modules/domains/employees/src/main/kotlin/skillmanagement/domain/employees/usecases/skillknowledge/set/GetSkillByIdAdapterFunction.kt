package skillmanagement.domain.employees.usecases.skillknowledge.set

import skillmanagement.domain.employees.model.SkillData
import skillmanagement.domain.employees.model.SkillId

interface GetSkillByIdAdapterFunction {
    operator fun invoke(id: SkillId): SkillData?
}
