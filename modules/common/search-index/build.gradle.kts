plugins {
    id("org.springframework.boot") apply false

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    api(project(":modules:common:model"))

    api("org.springframework.boot:spring-boot-starter-actuator")
    api("org.springframework.boot:spring-boot-starter-hateoas")
    api("org.elasticsearch.client:elasticsearch-rest-high-level-client")

    testImplementation(project(":modules:testing:common"))
}
