dependencies {
    api(project(":modules:common:core"))
    api(project(":modules:common:domain-base"))
    api(project(":modules:testing:core"))

    api("com.h2database:h2")
    api("com.squareup.okhttp3:okhttp")
    api("com.fasterxml.jackson.module:jackson-module-kotlin")
    api("io.rest-assured:rest-assured")

    api("org.testcontainers:testcontainers")
    api("com.graphql-java-kickstart:graphql-spring-boot-starter-test")

    api("org.springframework.boot:spring-boot-starter-test")
    api("org.springframework.restdocs:spring-restdocs-mockmvc")
    api("org.springframework.hateoas:spring-hateoas")
}
