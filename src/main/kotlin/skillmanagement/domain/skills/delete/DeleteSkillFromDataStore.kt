package skillmanagement.domain.skills.delete

import skillmanagement.domain.skills.SkillRepository
import skillmanagement.domain.TechnicalFunction
import java.util.*

@TechnicalFunction
class DeleteSkillFromDataStore(
    private val repository: SkillRepository
) {

    operator fun invoke(id: UUID) {
        repository.deleteById(id)
    }

}
