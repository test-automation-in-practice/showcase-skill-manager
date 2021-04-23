subprojects {
    tasks {
        register<Test>("unit-tests") {
            useJUnitPlatform { includeTags("unit-test") }
        }
        register<Test>("integration-tests") {
            shouldRunAfter("unit-tests")
            useJUnitPlatform { includeTags("integration-test") }
        }
        register<Test>("end2end-tests") {
            shouldRunAfter("integration-tests")
            useJUnitPlatform { includeTags("end2end-test") }
        }
        test {
            dependsOn("unit-tests", "integration-tests", "end2end-tests")
            useJUnitPlatform { excludeTags("unit-test", "integration-test", "end2end-test") }
        }
    }
}
