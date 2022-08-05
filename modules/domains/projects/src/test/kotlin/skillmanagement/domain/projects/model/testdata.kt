package skillmanagement.domain.projects.model

import org.intellij.lang.annotations.Language
import skillmanagement.common.model.Suggestion
import skillmanagement.test.instant
import skillmanagement.test.uuid

// Example #1

/** All possible properties are set (max example). */
internal val project_neo = ProjectEntity(
    id = projectId("f804d83f-466c-4eab-a58f-4b25ca1778f3"),
    version = 1,
    data = Project(
        label = ProjectLabel("Neo"),
        description = ProjectDescription("The PlayStation 4 Pro.")
    ),
    created = instant("2021-03-11T12:34:56.789Z"),
    lastUpdate = instant("2021-03-11T12:34:56.789Z")
)

@Language("json")
internal val project_neo_json = """
    {
      "id": "f804d83f-466c-4eab-a58f-4b25ca1778f3",
      "version": 1,
      "data": {
        "label": "Neo",
        "description": "The PlayStation 4 Pro."
      },
      "created": "2021-03-11T12:34:56.789Z",
      "lastUpdate": "2021-03-11T12:34:56.789Z"
    }
    """.trimIndent()

internal val project_representation_neo = project_neo.toRepresentation()

@Language("json")
internal val project_representation_neo_json = """
    {
      "id": "f804d83f-466c-4eab-a58f-4b25ca1778f3",
      "label": "Neo",
      "description": "The PlayStation 4 Pro.",
      "links": []
    }
    """.trimIndent()

internal val project_creation_data_neo = project_neo.toCreationData()

@Language("json")
internal val project_creation_data_neo_json = """
    {
      "label": "Neo",
      "description": "The PlayStation 4 Pro."
    }
    """.trimIndent()

internal val project_change_data_neo = project_neo.toChangeData()

@Language("json")
internal val project_change_data_neo_json = """
    {
      "label": "Neo",
      "description": "The PlayStation 4 Pro."
    }
    """.trimIndent()

internal val project_suggestion_neo = project_neo.toSuggestion()

// Example #2

/** Only required and some optional properties are set (medium example). */
internal val project_orbis = ProjectEntity(
    id = projectId("dce233f1-7c20-4250-817e-6676485ddb6e"),
    version = 1,
    data = Project(
        label = ProjectLabel("Orbis"),
        description = ProjectDescription("The PlayStation 4.")
    ),
    created = instant("2020-07-14T12:34:56.789Z"),
    lastUpdate = instant("2020-07-14T12:34:56.789Z")
)

@Language("json")
internal val project_orbis_json = """
    {
      "id": "dce233f1-7c20-4250-817e-6676485ddb6e",
      "version": 1,
      "data": {
        "label": "Orbis",
        "description": "The PlayStation 4."
      },
      "created": "2020-07-14T12:34:56.789Z",
      "lastUpdate": "2020-07-14T12:34:56.789Z"
    }
    """.trimIndent()

internal val project_representation_orbis = project_orbis.toRepresentation()

@Language("json")
internal val project_representation_orbis_json = """
    {
      "id": "dce233f1-7c20-4250-817e-6676485ddb6e",
      "label": "Orbis",
      "description": "The PlayStation 4.",
      "links": []
    }
    """.trimIndent()

internal val project_creation_data_orbis = project_orbis.toCreationData()

@Language("json")
internal val project_creation_data_orbis_json = """
    {
      "label": "Orbis",
      "description": "The PlayStation 4."
    }
    """.trimIndent()

internal val project_change_data_orbis = project_orbis.toChangeData()

@Language("json")
internal val project_change_data_orbis_json = """
    {
      "label": "Orbis",
      "description": "The PlayStation 4."
    }
    """.trimIndent()

internal val project_suggestion_orbis = project_orbis.toSuggestion()

// Example #3

/** Only required properties are set (min example). */
internal val project_morpheus = ProjectEntity(
    id = projectId("d5370813-a4cb-42d5-9d28-ce624c718538"),
    version = 1,
    data = Project(
        label = ProjectLabel("Morpheus"),
        description = ProjectDescription("The PlayStation VR Headset.")
    ),
    created = instant("2020-07-14T12:34:56.789Z"),
    lastUpdate = instant("2020-07-14T12:34:56.789Z")
)

@Language("json")
internal val project_morpheus_json = """
    {
      "id": "d5370813-a4cb-42d5-9d28-ce624c718538",
      "version": 1,
      "data": {
        "label": "Morpheus",
        "description": "The PlayStation VR Headset."
      },
      "created": "2020-07-14T12:34:56.789Z",
      "lastUpdate": "2020-07-14T12:34:56.789Z"
    }
    """.trimIndent()

internal val project_representation_morpheus = project_morpheus.toRepresentation()

@Language("json")
internal val project_representation_morpheus_json = """
    {
      "id": "d5370813-a4cb-42d5-9d28-ce624c718538",
      "label": "Morpheus",
      "description": "The PlayStation VR Headset.",
      "links": []
    }
    """.trimIndent()

internal val project_creation_data_morpheus = project_morpheus.toCreationData()

@Language("json")
internal val project_creation_data_morpheus_json = """
    {
      "label": "Morpheus",
      "description": "The PlayStation VR Headset."
    }
    """.trimIndent()

internal val project_change_data_morpheus = project_morpheus.toChangeData()

@Language("json")
internal val project_change_data_morpheus_json = """
    {
      "label": "Morpheus",
      "description": "The PlayStation VR Headset."
    }
    """.trimIndent()

internal val project_suggestion_morpheus = project_morpheus.toSuggestion()

// Functions

fun projectId() = ProjectId(uuid())

private fun ProjectEntity.toCreationData() =
    ProjectCreationData(label = data.label, description = data.description)

private fun ProjectEntity.toChangeData() = data.toChangeData()

internal fun ProjectEntity.toSuggestion() =
    Suggestion(id = id, label = data.label.toString())

internal fun ProjectRepresentation.toSuggestion() =
    Suggestion(id = id, label = label.toString())

internal fun project(
    id: String = uuid().toString(),
    version: Int = 1,
    label: String = "project",
    description: String = "description",
    created: String = "2020-08-13T12:34:56.789Z",
    lastUpdate: String = created
) = ProjectEntity(
    id = projectId(id),
    version = version,
    data = Project(
        label = ProjectLabel(label),
        description = ProjectDescription(description)
    ),
    created = instant(created),
    lastUpdate = instant(lastUpdate)
)

/**
 * Creates an instance based on this [ProjectEntity] which contains only those
 * properties that would be set after creating a new [ProjectEntity] based on
 * [ProjectCreationData].
 */
internal fun ProjectEntity.asFreshlyCreatedInstance() =
    ProjectEntity(
        id = id,
        version = 1,
        data = Project(
            label = data.label,
            description = data.description
        ),
        created = created,
        lastUpdate = created
    )
