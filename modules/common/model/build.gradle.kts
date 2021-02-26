plugins {
    id("org.springframework.boot") apply false

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    api(kotlin("reflect"))
    api(kotlin("stdlib-jdk8"))

    api("io.github.microutils:kotlin-logging:2.0.4")
    api("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.springframework.boot:spring-boot-starter") // TODO: just annotations?
    implementation("org.springframework.boot:spring-boot-starter-web") // TODO: just annotations?
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc") // TODO: just annotations?
    testImplementation(project(":modules:testing:common"))
}
