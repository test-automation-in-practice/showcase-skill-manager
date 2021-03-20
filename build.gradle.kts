import io.gitlab.arturbosch.detekt.detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript { repositories { mavenCentral(); jcenter() } }

plugins {
    id("org.springframework.boot") version "2.4.3" apply false
    id("io.spring.dependency-management") version "1.0.8.RELEASE" apply false
    id("org.asciidoctor.jvm.convert") version "3.3.0" apply false
    id("io.gitlab.arturbosch.detekt") version "1.16.0"

    kotlin("jvm") version "1.4.30" apply false
    kotlin("plugin.spring") version "1.4.30" apply false
}

allprojects {
    extra["okhttp3.version"] = "4.9.1"
    extra["rest-assured.version"] = "4.3.3"

    repositories { mavenCentral(); jcenter() }

    apply {
        plugin("io.gitlab.arturbosch.detekt")
    }

    tasks {
        withType<JavaCompile> {
            sourceCompatibility = "11"
            targetCompatibility = "11"
        }
        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "11"
            }
        }
        withType<Test> {
            useJUnitPlatform()
        }
    }

    detekt {
        config = rootProject.files("detekt.yml")
        buildUponDefaultConfig = true
        ignoreFailures = false
        reports {
            html { enabled = true }
            xml { enabled = false }
            txt { enabled = false }
        }
    }
}
