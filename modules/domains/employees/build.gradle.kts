plugins {
    id("org.springframework.boot") apply false

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    api(project(":modules:common:core"))
    api(project(":modules:common:domain-base"))

    api(project(":modules:domains:projects"))
    api(project(":modules:domains:skills"))

    runtimeOnly("org.postgresql:postgresql")

    testImplementation(project(":modules:testing:core"))
    testImplementation(project(":modules:testing:domain-base"))
}
