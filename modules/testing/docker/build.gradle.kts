plugins {
    id("org.springframework.boot") apply false

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    api(project(":modules:testing:common"))
    api("org.testcontainers:testcontainers:1.15.1")
    implementation("org.springframework.boot:spring-boot-starter-test") // TODO: just resources?
}
