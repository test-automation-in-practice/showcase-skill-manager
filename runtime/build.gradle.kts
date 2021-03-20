plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.asciidoctor.jvm.convert")

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":modules:domains:employees"))
    implementation(project(":modules:domains:projects"))
    implementation(project(":modules:domains:skills"))
    implementation("org.springframework.boot:spring-boot-starter-security")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation(project(":modules:testing:domain-base"))
    testImplementation("org.springframework.security:spring-security-test")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks {
    asciidoctor {
        dependsOn("test")
        dependsOn(":modules:domains:employees:asciidoctor")
        dependsOn(":modules:domains:projects:asciidoctor")
        dependsOn(":modules:domains:skills:asciidoctor")

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
        modules { diagram.use() }
    }
    bootJar {
        dependsOn("asciidoctor")
        from("$rootDir/modules/domains/employees/build/docs/asciidoc/api.html") {
            into("BOOT-INF/classes/static/docs/employees")
        }
        from("$rootDir/modules/domains/projects/build/docs/asciidoc/api.html") {
            into("BOOT-INF/classes/static/docs/projects")
        }
        from("$rootDir/modules/domains/skills/build/docs/asciidoc/api.html") {
            into("BOOT-INF/classes/static/docs/skills")
        }
        from(file("build/docs/asciidoc/api.html")) {
            into("BOOT-INF/classes/static/docs")
        }
    }
}
