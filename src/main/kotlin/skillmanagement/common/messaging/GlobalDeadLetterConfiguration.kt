package skillmanagement.common.messaging

import org.springframework.amqp.core.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

internal const val DEAD_LETTER_EXCHANGE_NAME = "global.dead-letters.exchange"
internal const val DEAD_LETTER_QUEUE_NAME = "global.dead-letters.queue"

@Configuration
class GlobalDeadLetterConfiguration {

    @Bean
    fun globalDeadLetterExchange(): FanoutExchange = FanoutExchange(DEAD_LETTER_EXCHANGE_NAME)

    @Bean
    fun globalDeadLetterQueue(): Queue = QueueBuilder.durable(DEAD_LETTER_QUEUE_NAME).build()

    @Bean
    fun globalDeadLetterBinding(): Binding = BindingBuilder.bind(globalDeadLetterQueue()).to(globalDeadLetterExchange())

}