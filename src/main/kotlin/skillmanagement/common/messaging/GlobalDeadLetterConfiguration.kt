package skillmanagement.common.messaging

import org.springframework.amqp.core.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

internal const val DEAD_LETTER_EXCHANGE = "$EXCHANGE_PREFIX.dead-letters"
internal const val DEAD_LETTER_QUEUE = "$QUEUE_PREFIX.dead-letters"
internal const val DEAD_LETTER_EXCHANGE_HEADER = "x-dead-letter-exchange"

@Configuration
class GlobalDeadLetterConfiguration {

    @Bean
    fun globalDeadLetterExchange(): FanoutExchange = FanoutExchange(DEAD_LETTER_EXCHANGE)

    @Bean
    fun globalDeadLetterQueue(): Queue = QueueBuilder.durable(DEAD_LETTER_QUEUE).build()

    @Bean
    fun globalDeadLetterBinding(): Binding = BindingBuilder.bind(globalDeadLetterQueue()).to(globalDeadLetterExchange())

}