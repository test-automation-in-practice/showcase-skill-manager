plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.asciidoctor.jvm.convert")

    kotlin("jvm")
    kotlin("plugin.spring")
}

repositories {
    mavenCentral()
}

dependencies {
//    implementation(project(":modules:commons"))
//    implementation(project(":modules:employees"))
//    implementation(project(":modules:projects"))
//    implementation(project(":modules:skills"))
//
//    testImplementation(project(":modules:testing"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.retry:spring-retry")

    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.flywaydb:flyway-core")

    implementation("com.github.java-json-tools:json-patch:1.13")
    implementation("io.github.microutils:kotlin-logging:2.0.4")
    implementation("org.elasticsearch.client:elasticsearch-rest-high-level-client")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("com.h2database:h2")

    testImplementation("io.kotlintest:kotlintest-assertions:3.4.2")
    testImplementation("io.rest-assured:rest-assured:4.3.3")
    testImplementation("com.squareup.okhttp3:okhttp:4.9.1")
    testImplementation("io.mockk:mockk:1.10.5")
    testImplementation("info.novatec.testit:testutils-logrecorder-logback:0.3.4")
    testImplementation("org.testcontainers:testcontainers:1.15.1")
    testImplementation("net.jqwik:jqwik:1.3.10")
    testImplementation("com.tngtech.archunit:archunit:0.16.0")
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

//    withType<Test> {
//        group = "verification"
//        useJUnitPlatform()
//        testLogging {
//            events(PASSED, FAILED, SKIPPED)
//        }
//    }
//
//    register<Test>("unit-tests") {
//        useJUnitPlatform { includeTags("unit-test") }
//    }
//
//    register<Test>("integration-tests") {
//        dependsOn("unit-tests")
//        useJUnitPlatform { includeTags("integration-test") }
//    }
//
//    register<Test>("end2end-tests") {
//        dependsOn("integration-tests")
//        useJUnitPlatform { includeTags("end2end-test") }
//    }
//
//    test {
//        dependsOn("end2end-tests")
//        useJUnitPlatform { excludeTags("unit-test", "integration-test", "end2end-test") }
//    }
}
