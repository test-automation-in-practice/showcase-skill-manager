package skillmanagement.domain.skills.find

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import skillmanagement.domain.TechnicalFunction
import skillmanagement.domain.skills.Skill
import skillmanagement.domain.skills.SkillDocument
import skillmanagement.domain.skills.SkillRepository
import skillmanagement.domain.skills.toSkill

@TechnicalFunction
class FindSkillsInDataStore(
    private val repository: SkillRepository
) {

    operator fun invoke(pageNumber: Int, pageSize: Int): Page<Skill> {
        val pageable = PageRequest.of(
            pageNumber,
            pageSize,
            Sort.Direction.ASC,
            "label"
        )
        return repository.findAll(pageable).map(SkillDocument::toSkill)
    }

}
