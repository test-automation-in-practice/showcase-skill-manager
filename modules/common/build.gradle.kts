plugins {
    id("org.springframework.boot") apply false

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    api(kotlin("reflect"))
    api(kotlin("stdlib-jdk8"))

    api("org.springframework.boot:spring-boot-starter")

    testImplementation(project(":modules:testing"))
}
