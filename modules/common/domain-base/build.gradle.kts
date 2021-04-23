dependencies {
    api(project(":modules:common:core"))

    api("org.springframework.boot:spring-boot-starter")
    api("org.springframework.boot:spring-boot-starter-actuator")
    api("org.springframework.boot:spring-boot-starter-aop")
    api("org.springframework.boot:spring-boot-starter-amqp")
    api("org.springframework.boot:spring-boot-starter-jdbc")
    api("org.springframework.boot:spring-boot-starter-hateoas")
    api("org.springframework.boot:spring-boot-starter-web")

    api("org.springframework.retry:spring-retry")

    api("org.flywaydb:flyway-core")
    api("io.micrometer:micrometer-registry-prometheus")
    api("com.fasterxml.jackson.module:jackson-module-kotlin")
    api("org.elasticsearch.client:elasticsearch-rest-high-level-client")

    api("com.github.java-json-tools:json-patch")
    api("com.graphql-java-kickstart:graphql-spring-boot-starter")

    implementation("org.springframework.security:spring-security-core")

    testImplementation(project(":modules:testing:core"))
}
