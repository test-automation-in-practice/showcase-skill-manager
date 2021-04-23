import org.springframework.boot.gradle.dsl.SpringBootExtension

plugins {
    id("org.springframework.boot") apply false
}

subprojects {
    apply {
        plugin("org.springframework.boot")
    }
    the<SpringBootExtension>().apply { buildInfo() }
}
