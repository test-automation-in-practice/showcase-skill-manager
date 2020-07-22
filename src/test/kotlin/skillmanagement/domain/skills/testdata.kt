package skillmanagement.domain.skills

import skillmanagement.test.uuid
import java.time.Instant
import java.util.Collections.emptySortedSet

val skill_kotlin = Skill(
    id = uuid("3f7985b9-f5f0-4662-bda9-1dcde01f5f3b"),
    version = 1,
    label = SkillLabel("Kotlin"),
    tags = sortedSetOf(Tag("language"), Tag("cool")),
    lastUpdate = Instant.parse("2020-07-14T12:34:56.789Z")
)
val skill_java = Skill(
    id = uuid("f8948935-dab6-4c33-80d0-9f66ae546a7c"),
    version = 1,
    label = SkillLabel("Java"),
    tags = sortedSetOf(Tag("language")),
    lastUpdate = Instant.parse("2020-07-14T12:34:56.789Z")
)
val skill_python = Skill(
    id = uuid("6935e550-d041-418a-9070-e37431069232"),
    version = 1,
    label = SkillLabel("Python"),
    tags = emptySortedSet(),
    lastUpdate = Instant.parse("2020-07-14T12:34:56.789Z")
)
