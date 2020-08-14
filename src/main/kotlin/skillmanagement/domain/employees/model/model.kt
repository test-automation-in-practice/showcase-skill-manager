package skillmanagement.domain.employees.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import skillmanagement.common.validation.Validation.Companion.validate
import skillmanagement.common.validation.isGreaterThanOrEqualTo
import skillmanagement.common.validation.isLessThanOrEqualTo
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.skills.model.Skill
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

// TODO:
//  - Zertifizierungen
//  - Publikationen
//  - "Beratungsschwerpunkt und Schlüsselqualifikationen"
//  - Beruflicher Werdegang
//     - von bis
//     - bis ist optional
//     - Firma + Titel
//  - Ausbildung
//     - Abschluss
//     - Sprachkennisse

data class Employee(
    val id: UUID,
    val version: Int,
    val firstName: FirstName,
    val lastName: LastName,
    val title: Title,
    val email: EmailAddress,
    val telephone: TelephoneNumber,
    val skills: List<SkillKnowledge>,
    val projects: List<ProjectAssignment>,
    val lastUpdate: Instant
)

data class FirstName @JsonCreator constructor(
    @JsonValue private val value: String
) {
    override fun toString() = value
}

data class LastName @JsonCreator constructor(
    @JsonValue private val value: String
) {
    override fun toString() = value
}

data class Title @JsonCreator constructor(
    @JsonValue private val value: String
) {
    override fun toString() = value
}

data class EmailAddress @JsonCreator constructor(
    @JsonValue private val value: String
) {
    override fun toString() = value
}

data class TelephoneNumber @JsonCreator constructor(
    @JsonValue private val value: String
) {
    override fun toString() = value
}

// TODO:
//  - Kategorie / Gruppe in der der Employee den Skill sieht

data class SkillKnowledge(
    val skill: Skill,
    val level: SkillLevel,
    val secret: Boolean
)

data class SkillLevel @JsonCreator constructor(
    @JsonValue private val value: Int
) {

    init {
        validate(value, "Skill Level") {
            isGreaterThanOrEqualTo(1)
            isLessThanOrEqualTo(10)
        }
    }

    fun toInt() = value
    override fun toString() = value.toString()

}

// TODO:
//  - Qualifikationen / Eingesetzte Skills

data class ProjectAssignment(
    val id: UUID,
    val project: Project,
    val contribution: ProjectContribution,
    val startDate: LocalDate,
    val endDate: LocalDate?
)

data class ProjectContribution @JsonCreator constructor(
    @JsonValue private val value: String
) {
    override fun toString() = value
}