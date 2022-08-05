dependencies {
    implementation(project(":modules:domains:employees"))
    implementation(project(":modules:domains:projects"))
    implementation(project(":modules:domains:skills"))
    implementation("org.springframework.boot:spring-boot-starter-security")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation(project(":modules:testing:testing-domain-base"))
    testImplementation("org.springframework.security:spring-security-test")
}

tasks {
    asciidoctor {
        shouldRunAfter("test")
        dependsOn(":modules:domains:employees:asciidoctor")
        dependsOn(":modules:domains:projects:asciidoctor")
        dependsOn(":modules:domains:skills:asciidoctor")
    }
    build {
        dependsOn(":modules:domains:employees:test")
        dependsOn(":modules:domains:projects:test")
        dependsOn(":modules:domains:skills:test")
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
