import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.plugin.SpringBootPlugin

buildscript {
    repositories { mavenCentral() }
}

plugins {
    id("org.springframework.boot") version "2.4.5" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("io.gitlab.arturbosch.detekt") version "1.16.0"

    kotlin("jvm") version "1.4.32"
    kotlin("plugin.spring") version "1.4.32"
}

apply {
    plugin("io.gitlab.arturbosch.detekt")
}

allprojects {
    extra["okhttp3.version"] = "4.9.1"
    extra["rest-assured.version"] = "4.3.3"

    repositories { mavenCentral() }

    apply {
        plugin("org.asciidoctor.jvm.convert")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("io.gitlab.arturbosch.detekt")
        plugin("io.spring.dependency-management")
    }

    dependencyManagement {
        imports {
            mavenBom(SpringBootPlugin.BOM_COORDINATES)
        }
        dependencies {
            // define versions of unmanaged dependencies
            dependency("com.github.java-json-tools:json-patch:1.13")
            dependency("com.graphql-java-kickstart:graphql-spring-boot-starter:11.0.0")
            dependency("com.graphql-java-kickstart:graphql-spring-boot-starter-test:11.0.0")
            dependency("com.sun.xml.bind:jaxb-osgi:2.3.4")
            dependency("com.tngtech.archunit:archunit:0.16.0")
            dependency("info.novatec.testit:logrecorder-assertions:1.0.0")
            dependency("info.novatec.testit:logrecorder-logback:1.0.0")
            dependency("io.arrow-kt:arrow-core:0.13.0")
            dependency("io.github.microutils:kotlin-logging:2.0.4")
            dependency("io.kotlintest:kotlintest-assertions:3.4.2")
            dependency("io.mockk:mockk:1.10.5")
            dependency("net.jqwik:jqwik:1.3.10")
            dependency("org.testcontainers:testcontainers:1.15.1")
        }
    }

    dependencies {
        constraints {
            detekt("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
        }
    }

    tasks {
        asciidoctor {
            mustRunAfter("test")
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
                    "toclevels" to "3",
                    "sectlinks" to "true",
                    "data-uri" to "true",
                    "nofooter" to "true"
                )
            )

        }
        asciidoctorj {
            fatalWarnings("include file not found")
            modules { diagram.use() }
        }
        build {
            dependsOn("asciidoctor")
        }
        the<DetektExtension>().apply {
            config = rootProject.files("detekt.yml")
            buildUponDefaultConfig = true
            ignoreFailures = false
            reports {
                html { enabled = true }
                xml { enabled = false }
                txt { enabled = false }
            }
        }
        withType<JavaCompile> {
            sourceCompatibility = "11"
            targetCompatibility = "11"
        }
        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
                jvmTarget = "11"
                javaParameters = true
                allWarningsAsErrors = true
            }
        }
        withType<Test> {
            group = "verification"
            useJUnitPlatform()
            testLogging { events(FAILED, SKIPPED) }
        }
    }
}
