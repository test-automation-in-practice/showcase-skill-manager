package skillmanagement.domain.skills.model

import skillmanagement.common.model.Suggestion
import skillmanagement.test.instant
import skillmanagement.test.uuid
import java.util.Collections.emptySortedSet

// Example #1

/** All possible properties are set (max example). */
internal val skill_kotlin = SkillEntity(
    id = skillId("3f7985b9-f5f0-4662-bda9-1dcde01f5f3b"),
    version = 1,
    data = Skill(
        label = SkillLabel("Kotlin"),
        description = SkillDescription("The coolest programming language."),
        tags = sortedSetOf(Tag("language"), Tag("cool"))
    ),
    created = instant("2020-07-14T12:34:56.789Z"),
    lastUpdate = instant("2020-07-14T12:34:56.789Z")
)
internal val skill_kotlin_json = """
    {
      "id": "3f7985b9-f5f0-4662-bda9-1dcde01f5f3b",
      "version": 1,
      "data": {
        "label": "Kotlin",
        "description": "The coolest programming language.",
        "tags": [ "cool", "language" ]
      },
      "created": "2020-07-14T12:34:56.789Z",
      "lastUpdate": "2020-07-14T12:34:56.789Z"
    }
    """.trimIndent()

internal val skill_representation_kotlin = skill_kotlin.toRepresentation()
internal val skill_representation_kotlin_json = """
    {
      "id": "3f7985b9-f5f0-4662-bda9-1dcde01f5f3b",
      "label": "Kotlin",
      "description": "The coolest programming language.",
      "tags": [ "cool", "language" ],
      "links": []
    }
    """.trimIndent()

internal val skill_creation_data_kotlin = skill_kotlin.toCreationData()
internal val skill_creation_data_kotlin_json = """
    {
      "label": "Kotlin",
      "description": "The coolest programming language.",
      "tags": [ "cool", "language" ]
    }
    """.trimIndent()

internal val skill_change_data_kotlin = skill_kotlin.toChangeData()
internal val skill_change_data_kotlin_json = """
    {
      "label": "Kotlin",
      "description": "The coolest programming language.",
      "tags": [ "cool", "language" ]
    }
    """.trimIndent()

internal val skill_suggestion_kotlin = skill_kotlin.toSuggestion()

// Example #2

/** Only required and some optional properties are set (medium example). */
internal val skill_java = SkillEntity(
    id = skillId("f8948935-dab6-4c33-80d0-9f66ae546a7c"),
    version = 1,
    data = Skill(
        label = SkillLabel("Java"),
        description = null,
        tags = sortedSetOf(Tag("language"))
    ),
    created = instant("2020-07-14T12:34:56.789Z"),
    lastUpdate = instant("2020-07-14T12:34:56.789Z")
)
internal val skill_java_json = """
    {
      "id": "f8948935-dab6-4c33-80d0-9f66ae546a7c",
      "version": 1,
      "data": {
        "label": "Java",
        "tags": [ "language" ]
      },
      "created": "2020-07-14T12:34:56.789Z",
      "lastUpdate": "2020-07-14T12:34:56.789Z"
    }
    """.trimIndent()

internal val skill_representation_java = skill_java.toRepresentation()
internal val skill_representation_java_json = """
    {
      "id": "f8948935-dab6-4c33-80d0-9f66ae546a7c",
      "label": "Java",
      "tags": [ "language" ],
      "links": []
    }
    """.trimIndent()

internal val skill_creation_data_java = skill_java.toCreationData()
internal val skill_creation_data_java_json = """
    {
      "label": "Java",
      "tags": [ "language" ]
    }
    """.trimIndent()

internal val skill_change_data_java = skill_java.toChangeData()
internal val skill_change_data_java_json = """
    {
      "label": "Java",
      "tags": [ "language" ]
    }
    """.trimIndent()

internal val skill_suggestion_java = skill_java.toSuggestion()

// Example #3

/** Only required properties are set (min example). */
internal val skill_python = SkillEntity(
    id = skillId("6935e550-d041-418a-9070-e37431069232"),
    version = 1,
    data = Skill(
        label = SkillLabel("Python"),
        description = null,
        tags = emptySortedSet()
    ),
    created = instant("2020-07-14T12:34:56.789Z"),
    lastUpdate = instant("2020-07-14T12:34:56.789Z")
)
internal val skill_python_json = """
    {
      "id": "6935e550-d041-418a-9070-e37431069232",
      "version": 1,
      "data": {
        "label": "Python",
        "tags": []
      },
      "created": "2020-07-14T12:34:56.789Z",
      "lastUpdate": "2020-07-14T12:34:56.789Z"
    }
    """.trimIndent()

internal val skill_representation_python = skill_python.toRepresentation()
internal val skill_representation_python_json = """
    {
      "id": "6935e550-d041-418a-9070-e37431069232",
      "label": "Python",
      "tags": [],
      "links": []
    }
    """.trimIndent()

internal val skill_creation_data_python = skill_python.toCreationData()
internal val skill_creation_data_python_json = """
    {
      "label": "Python",
      "tags": []
    }
    """.trimIndent()

internal val skill_change_data_python = skill_python.toChangeData()
internal val skill_change_data_python_json = """
    {
      "label": "Python",
      "tags": []
    }
    """.trimIndent()

internal val skill_suggestion_python = skill_python.toSuggestion()

// Functions

fun skillId() = SkillId(uuid())

private fun SkillEntity.toCreationData() =
    SkillCreationData(data.label, data.description, data.tags)

private fun SkillEntity.toChangeData() = data.toChangeData()

internal fun SkillEntity.toSuggestion() =
    Suggestion(id = id, label = data.label.toString())

internal fun SkillRepresentation.toSuggestion() =
    Suggestion(id = id, label = label.toString())

internal fun skill(
    id: String = uuid().toString(),
    version: Int = 1,
    label: String = "skill",
    description: String? = null,
    tags: Collection<String> = emptyList(),
    created: String = "2020-08-13T12:34:56.789Z",
    lastUpdate: String = created
) = SkillEntity(
    id = skillId(id),
    version = version,
    data = Skill(
        label = SkillLabel(label),
        description = description?.let(::SkillDescription),
        tags = tags.map(::Tag).toSortedSet()
    ),
    created = instant(created),
    lastUpdate = instant(lastUpdate)
)

/**
 * Creates an instance based on this [SkillEntity] which contains only those
 * properties that would be set after creating a new [SkillEntity] based on
 * [SkillCreationData].
 */
internal fun SkillEntity.asFreshlyCreatedInstance() =
    SkillEntity(
        id = id,
        version = 1,
        data = Skill(
            label = data.label,
            description = data.description,
            tags = data.tags
        ),
        created = created,
        lastUpdate = created
    )
