dependencies {
    api("org.junit.jupiter:junit-jupiter")

    api("net.jqwik:jqwik")
    api("io.mockk:mockk")
    api("org.assertj:assertj-core")
    api("io.kotlintest:kotlintest-assertions") {
        exclude(group = "io.arrow-kt") // TODO
    }
    api("info.novatec.testit:testutils-logrecorder-logback")
    api("com.tngtech.archunit:archunit")

    implementation("org.springframework.boot:spring-boot-starter-test")
}
