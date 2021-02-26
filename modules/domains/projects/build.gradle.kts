plugins {
    id("org.springframework.boot") apply false

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    api(project(":modules:common:event-publishing"))
    api(project(":modules:common:model"))
    api(project(":modules:common:search-index"))
    api(project(":modules:common:web"))

    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.boot:spring-boot-starter-actuator")
    api("org.springframework.boot:spring-boot-starter-aop")
    api("org.springframework.boot:spring-boot-starter-amqp")
    api("org.springframework.boot:spring-boot-starter-jdbc")
    api("org.springframework.boot:spring-boot-starter-hateoas")
    api("org.springframework.boot:spring-boot-starter-web")

    api("org.springframework.retry:spring-retry")
    api("org.flywaydb:flyway-core")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation(project(":modules:testing:common"))
    testImplementation(project(":modules:testing:docker"))
    testImplementation(project(":modules:testing:model"))
    testImplementation(project(":modules:testing:web"))

    testImplementation("com.squareup.okhttp3:okhttp:4.9.1")
}
