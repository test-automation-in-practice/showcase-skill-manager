package skillmanagement.domain.projects.model

import skillmanagement.common.model.Suggestion
import skillmanagement.test.instant
import skillmanagement.test.uuid

// Example #1

/** All possible properties are set (max example). */
internal val project_neo = Project(
    id = uuid("f804d83f-466c-4eab-a58f-4b25ca1778f3"),
    version = 1,
    label = ProjectLabel("Neo"),
    description = ProjectDescription("The PlayStation 4 Pro."),
    lastUpdate = instant("2021-03-11T12:34:56.789Z")
)
internal val project_neo_json = """
    {
      "id": "f804d83f-466c-4eab-a58f-4b25ca1778f3",
      "version": 1,
      "label": "Neo",
      "description": "The PlayStation 4 Pro.",
      "lastUpdate": "2021-03-11T12:34:56.789Z"
    }
    """.trimIndent()

internal val project_resource_neo = project_neo.toResourceWithoutLinks()
internal val project_resource_neo_json = """
    {
      "id": "f804d83f-466c-4eab-a58f-4b25ca1778f3",
      "label": "Neo",
      "description": "The PlayStation 4 Pro.",
      "links": []
    }
    """.trimIndent()

internal val project_change_data_neo = project_neo.toChangeData()
internal val project_change_data_neo_json = """
    {
      "label": "Neo",
      "description": "The PlayStation 4 Pro."
    }
    """.trimIndent()

internal val project_neo_suggestion = project_neo.toSuggestion()

// Example #2

/** Only required and some optional properties are set (medium example). */
internal val project_orbis = Project(
    id = uuid("dce233f1-7c20-4250-817e-6676485ddb6e"),
    version = 1,
    label = ProjectLabel("Orbis"),
    description = ProjectDescription("The PlayStation 4."),
    lastUpdate = instant("2020-07-14T12:34:56.789Z")
)
internal val project_orbis_json = """
    {
      "id": "dce233f1-7c20-4250-817e-6676485ddb6e",
      "version": 1,
      "label": "Orbis",
      "description": "The PlayStation 4.",
      "lastUpdate": "2020-07-14T12:34:56.789Z"
    }
    """.trimIndent()

internal val project_resource_orbis = project_orbis.toResourceWithoutLinks()
internal val project_resource_orbis_json = """
    {
      "id": "dce233f1-7c20-4250-817e-6676485ddb6e",
      "label": "Orbis",
      "description": "The PlayStation 4.",
      "links": []
    }
    """.trimIndent()

internal val project_change_data_orbis = project_orbis.toChangeData()
internal val project_change_data_orbis_json = """
    {
      "label": "Orbis",
      "description": "The PlayStation 4."
    }
    """.trimIndent()

internal val project_orbis_suggestion = project_orbis.toSuggestion()

// Example #3

/** Only required properties are set (min example). */
internal val project_morpheus = Project(
    id = uuid("d5370813-a4cb-42d5-9d28-ce624c718538"),
    version = 1,
    label = ProjectLabel("Morpheus"),
    description = ProjectDescription("The PlayStation VR Headset."),
    lastUpdate = instant("2020-07-14T12:34:56.789Z")
)
internal val project_morpheus_json = """
    {
      "id": "d5370813-a4cb-42d5-9d28-ce624c718538",
      "version": 1,
      "label": "Morpheus",
      "description": "The PlayStation VR Headset.",
      "lastUpdate": "2020-07-14T12:34:56.789Z"
    }
    """.trimIndent()

internal val project_resource_morpheus = project_morpheus.toResourceWithoutLinks()
internal val project_resource_morpheus_json = """
    {
      "id": "d5370813-a4cb-42d5-9d28-ce624c718538",
      "label": "Morpheus",
      "description": "The PlayStation VR Headset.",
      "links": []
    }
    """.trimIndent()

internal val project_change_data_morpheus = project_morpheus.toChangeData()
internal val project_change_data_morpheus_json = """
    {
      "label": "Morpheus",
      "description": "The PlayStation VR Headset."
    }
    """.trimIndent()

internal val project_morpheus_suggestion = project_morpheus.toSuggestion()

// Functions

private fun Project.toResourceWithoutLinks() =
    ProjectResource(id = id, label = label, description = description)

internal fun Project.toSuggestion() =
    Suggestion(id = id, label = label.toString())

internal fun ProjectResource.toSuggestion() =
    Suggestion(id = id, label = label.toString())

internal fun project(
    id: String = uuid().toString(),
    version: Int = 1,
    label: String = "project",
    description: String = "description",
    lastUpdate: String = "2020-08-13T12:34:56.789Z"
) = Project(
    id = uuid(id),
    version = version,
    label = ProjectLabel(label),
    description = ProjectDescription(description),
    lastUpdate = instant(lastUpdate)
)
