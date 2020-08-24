package skillmanagement.domain.employees.model

import skillmanagement.common.model.IntType
import skillmanagement.common.model.Name
import skillmanagement.common.model.StringType
import skillmanagement.common.model.Text
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

class FirstName(value: String) : Name(value)
class LastName(value: String) : Name(value)
class Title(value: String) : StringType(value)
class EmailAddress(value: String) : StringType(value)
class TelephoneNumber(value: String) : StringType(value)

// TODO:
//  - Kategorie / Gruppe in der der Employee den Skill sieht

data class SkillKnowledge(
    val skill: Skill,
    val level: SkillLevel,
    val secret: Boolean
)

class SkillLevel(value: Int) : IntType(value, min = 1, max = 10)

// TODO:
//  - Qualifikationen / Eingesetzte Skills

data class ProjectAssignment(
    val id: UUID,
    val project: Project,
    val contribution: ProjectContribution,
    val startDate: LocalDate,
    val endDate: LocalDate?
)

class ProjectContribution(value: String) : Text(value)
