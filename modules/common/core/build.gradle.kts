dependencies {
    api(kotlin("reflect"))
    api(kotlin("stdlib-jdk8"))

    api("com.fasterxml.jackson.core:jackson-annotations")

    api("io.arrow-kt:arrow-core")
    api("io.github.microutils:kotlin-logging")

    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")
    implementation("org.springframework:spring-web")

    testImplementation(project(":modules:testing:testing-core"))
}
