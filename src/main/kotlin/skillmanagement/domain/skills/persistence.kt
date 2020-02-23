package skillmanagement.domain.skills

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface SkillRepository : MongoRepository<SkillDocument, UUID>

@Document("skills")
data class SkillDocument(
    @Id val id: UUID,
    val label: SkillLabel
)

fun SkillDocument.toSkill() = Skill(
    id = id,
    label = label
)

fun Skill.toDocument() = SkillDocument(
    id = id,
    label = label
)
