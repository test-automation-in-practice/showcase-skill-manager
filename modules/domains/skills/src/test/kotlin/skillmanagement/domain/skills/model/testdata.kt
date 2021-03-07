package skillmanagement.domain.skills.model

import skillmanagement.common.model.Suggestion
import skillmanagement.test.instant
import skillmanagement.test.uuid
import java.util.Collections.emptySortedSet

// Example #1

/** All possible properties are set (max example). */
internal val skill_kotlin = Skill(
    id = uuid("3f7985b9-f5f0-4662-bda9-1dcde01f5f3b"),
    version = 1,
    label = SkillLabel("Kotlin"),
    description = SkillDescription("The coolest programming language."),
    tags = sortedSetOf(Tag("language"), Tag("cool")),
    lastUpdate = instant("2020-07-14T12:34:56.789Z")
)
internal val skill_kotlin_json = """
    {
      "id": "3f7985b9-f5f0-4662-bda9-1dcde01f5f3b",
      "version": 1,
      "label": "Kotlin",
      "description": "The coolest programming language.",
      "tags": [ "cool", "language" ],
      "lastUpdate": "2020-07-14T12:34:56.789Z"
    }
    """.trimIndent()

internal val skill_resource_kotlin = skill_kotlin.toResourceWithoutLinks()
internal val skill_resource_kotlin_json = """
    {
      "id": "3f7985b9-f5f0-4662-bda9-1dcde01f5f3b",
      "label": "Kotlin",
      "description": "The coolest programming language.",
      "tags": [ "cool", "language" ],
      "links": []
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

internal val skill_kotlin_suggestion = skill_kotlin.toSuggestion()

// Example #2

/** Only required and some optional properties are set (medium example). */
internal val skill_java = Skill(
    id = uuid("f8948935-dab6-4c33-80d0-9f66ae546a7c"),
    version = 1,
    label = SkillLabel("Java"),
    description = null,
    tags = sortedSetOf(Tag("language")),
    lastUpdate = instant("2020-07-14T12:34:56.789Z")
)
internal val skill_java_json = """
    {
      "id": "f8948935-dab6-4c33-80d0-9f66ae546a7c",
      "version": 1,
      "label": "Java",
      "tags": [ "language" ],
      "lastUpdate": "2020-07-14T12:34:56.789Z"
    }
    """.trimIndent()

internal val skill_resource_java = skill_java.toResourceWithoutLinks()
internal val skill_resource_java_json = """
    {
      "id": "f8948935-dab6-4c33-80d0-9f66ae546a7c",
      "label": "Java",
      "tags": [ "language" ],
      "links": []
    }
    """.trimIndent()

internal val skill_change_data_java = skill_java.toChangeData()
internal val skill_change_data_java_json = """
    {
      "label": "Java",
      "tags": [ "language" ]
    }
    """.trimIndent()

internal val skill_java_suggestion = skill_java.toSuggestion()

// Example #3

/** Only required properties are set (min example). */
internal val skill_python = Skill(
    id = uuid("6935e550-d041-418a-9070-e37431069232"),
    version = 1,
    label = SkillLabel("Python"),
    description = null,
    tags = emptySortedSet(),
    lastUpdate = instant("2020-07-14T12:34:56.789Z")
)
internal val skill_python_json = """
    {
      "id": "6935e550-d041-418a-9070-e37431069232",
      "version": 1,
      "label": "Python",
      "tags": [],
      "lastUpdate": "2020-07-14T12:34:56.789Z"
    }
    """.trimIndent()

internal val skill_resource_python = skill_python.toResourceWithoutLinks()
internal val skill_resource_python_json = """
    {
      "id": "6935e550-d041-418a-9070-e37431069232",
      "label": "Python",
      "tags": [],
      "links": []
    }
    """.trimIndent()

internal val skill_change_data_python = skill_python.toChangeData()
internal val skill_change_data_python_json = """
    {
      "label": "Python",
      "tags": []
    }
    """.trimIndent()

internal val skill_python_suggestion = skill_python.toSuggestion()

// Functions

private fun Skill.toResourceWithoutLinks() =
    SkillResource(id = id, label = label, description = description, tags = tags)

internal fun Skill.toSuggestion() =
    Suggestion(id = id, label = label.toString())

internal fun SkillResource.toSuggestion() =
    Suggestion(id = id, label = label.toString())

internal fun skill(
    id: String = uuid().toString(),
    version: Int = 1,
    label: String = "skill",
    description: String? = null,
    tags: Collection<String> = emptyList(),
    lastUpdate: String = "2020-08-13T12:34:56.789Z"
) = Skill(
    id = uuid(id),
    version = version,
    label = SkillLabel(label),
    description = description?.let(::SkillDescription),
    tags = tags.map(::Tag).toSortedSet(),
    lastUpdate = instant(lastUpdate)
)
