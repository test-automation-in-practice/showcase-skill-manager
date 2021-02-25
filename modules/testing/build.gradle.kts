plugins {
    id("org.springframework.boot") apply false

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    api(kotlin("reflect"))
    api(kotlin("stdlib-jdk8"))
}
