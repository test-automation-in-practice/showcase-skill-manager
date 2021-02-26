plugins {
    id("org.springframework.boot") apply false

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    api(project(":modules:common:core"))
    api(project(":modules:common:domain-base"))

    runtimeOnly("org.postgresql:postgresql")

    testImplementation(project(":modules:testing:core"))
    testImplementation(project(":modules:testing:domain-base"))
}
