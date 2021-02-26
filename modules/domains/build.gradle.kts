import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED

plugins {
    id("org.asciidoctor.jvm.convert")
    kotlin("jvm")
}

subprojects {
    repositories {
        mavenCentral()
    }

    apply {
        plugin("org.asciidoctor.jvm.convert")
        plugin("org.jetbrains.kotlin.jvm")
    }

    tasks {
        asciidoctor {
            baseDirFollowsSourceDir()
            options(
                mapOf(
                    "doctype" to "book",
                    "backend" to "html5"
                )
            )
            attributes(
                mapOf(
                    "snippets" to file("$buildDir/generated-snippets"),
                    "source-highlighter" to "coderay",
                    "toc" to "left",
                    "toclevels" to "3",
                    "sectlinks" to "true"
                )
            )

        }
        asciidoctorj {
            fatalWarnings("include file not found")
        }

        withType<Test> {
            group = "verification"
            useJUnitPlatform()
            testLogging {
                events(PASSED, FAILED, SKIPPED)
            }
        }

        register<Test>("unit-tests") {
            useJUnitPlatform { includeTags("unit-test") }
        }

        register<Test>("integration-tests") {
            dependsOn("unit-tests")
            useJUnitPlatform { includeTags("integration-test") }
        }

        register<Test>("end2end-tests") {
            dependsOn("integration-tests")
            useJUnitPlatform { includeTags("end2end-test") }
        }

        test {
            dependsOn("end2end-tests")
            useJUnitPlatform { excludeTags("unit-test", "integration-test", "end2end-test") }
        }
    }
}
