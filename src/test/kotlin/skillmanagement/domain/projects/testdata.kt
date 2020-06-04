package skillmanagement.domain.projects

import skillmanagement.test.uuid

val project_starlink = Project(
    id = uuid("15c1c1d8-3445-4d95-a945-605aaf087078"),
    label = ProjectLabel("Starlink satellite"),
    description = ProjectDescription("Lorem ipsum dolor sit amet ..")
)

val project_demo_2 = Project(
    id = uuid("d61c7840-7be3-4668-ae48-6c0b396268b5"),
    label = ProjectLabel("Demo-2"),
    description = ProjectDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed ..")
)
