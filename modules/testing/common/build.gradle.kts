plugins {
    id("org.springframework.boot") apply false

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    api("org.junit.jupiter:junit-jupiter")
    api("net.jqwik:jqwik:1.3.10")
    api("io.mockk:mockk:1.10.5")
    api("org.assertj:assertj-core")

    implementation("org.springframework.boot:spring-boot-starter-test") // TODO: just annotations?
}
