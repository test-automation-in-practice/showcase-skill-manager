plugins {
    id("org.springframework.boot") apply false

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    api(kotlin("reflect"))
    api(kotlin("stdlib-jdk8"))

    api("com.fasterxml.jackson.core:jackson-annotations")

    api("io.arrow-kt:arrow-core:0.13.0")
    api("io.github.microutils:kotlin-logging:2.0.4")

    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
    implementation("org.springframework:spring-web")

    testImplementation(project(":modules:testing:core"))
}
