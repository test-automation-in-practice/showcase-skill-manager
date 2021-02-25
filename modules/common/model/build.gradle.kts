plugins {
    id("org.springframework.boot") apply false

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    api(kotlin("reflect"))
    api(kotlin("stdlib-jdk8"))

    api("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation(project(":modules:testing:common"))
}
