package skillmanagement.domain.projects.model

import skillmanagement.common.model.Suggestion
import skillmanagement.test.instant
import skillmanagement.test.uuid
import java.time.Instant

val project_starlink = Project(
    id = uuid("15c1c1d8-3445-4d95-a945-605aaf087078"),
    version = 1,
    label = ProjectLabel("Starlink satellite"),
    description = ProjectDescription("Lorem ipsum dolor sit amet .."),
    lastUpdate = Instant.parse("2020-07-14T12:34:56.789Z")
)

val project_demo_2 = Project(
    id = uuid("d61c7840-7be3-4668-ae48-6c0b396268b5"),
    version = 1,
    label = ProjectLabel("Demo-2"),
    description = ProjectDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed .."),
    lastUpdate = Instant.parse("2020-07-14T12:34:56.789Z")
)

// Functions

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
