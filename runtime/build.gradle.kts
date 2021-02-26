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
    implementation(project(":modules:domains:employees"))
    implementation(project(":modules:domains:projects"))
    implementation(project(":modules:domains:skills"))

    runtimeOnly("org.postgresql:postgresql")

    testImplementation(project(":modules:testing:domain-base"))
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
        modules { diagram.use() }
    }
}
