plugins {
    id("org.springframework.boot") apply false

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    api(project(":modules:common:core"))
    api(project(":modules:testing:core"))

    api("com.h2database:h2")
    api("com.squareup.okhttp3:okhttp")
    api("com.fasterxml.jackson.module:jackson-module-kotlin")

    api("org.testcontainers:testcontainers:1.15.1")
    api("io.rest-assured:rest-assured:4.3.3")
    api("com.tngtech.archunit:archunit:0.16.0")

    api("org.springframework.boot:spring-boot-starter-test")
    api("org.springframework.restdocs:spring-restdocs-mockmvc")
    api("org.springframework.hateoas:spring-hateoas")
}
