plugins {
    id("org.asciidoctor.jvm.convert")
}

subprojects {
    repositories {
        mavenCentral()
    }

    apply {
        plugin("org.asciidoctor.jvm.convert")
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
    }
}
