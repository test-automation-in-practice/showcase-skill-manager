package skillmanagement

import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.util.IdGenerator
import org.springframework.util.JdkIdGenerator
import java.time.Clock


@EnableAsync
@EnableRetry
@EnableScheduling
@SpringBootApplication
@EnableHypermediaSupport(type = [HAL])
class Application

@Configuration
class ApplicationConfiguration {

    @Bean
    fun clock(): Clock = Clock.systemUTC()

    @Bean
    fun idGenerator(): IdGenerator = JdkIdGenerator()

    @Bean
    fun client(): RestHighLevelClient = // TODO: externalize configuration
        RestHighLevelClient(RestClient.builder(HttpHost.create("localhost:9200")))
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
