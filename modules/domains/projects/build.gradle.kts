dependencies {
    api(project(":modules:common:core"))
    api(project(":modules:common:domain-base"))

    runtimeOnly("org.postgresql:postgresql")

    testImplementation(project(":modules:testing:testing-core"))
    testImplementation(project(":modules:testing:testing-domain-base"))
}
