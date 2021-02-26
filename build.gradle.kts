import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript { repositories { mavenCentral() } }

plugins {
    id("org.springframework.boot") version "2.4.3" apply false
    id("io.spring.dependency-management") version "1.0.8.RELEASE" apply false
    id("org.asciidoctor.jvm.convert") version "3.3.0" apply false

    kotlin("jvm") version "1.4.30" apply false
    kotlin("plugin.spring") version "1.4.30" apply false
}

allprojects {
    extra["okhttp3.version"] = "4.9.1"

    tasks.withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
