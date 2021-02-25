plugins {
    id("org.springframework.boot") apply false

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    api(project(":modules:common"))

    api("org.springframework.boot:spring-boot-starter")

    testImplementation(project(":modules:testing"))
}
