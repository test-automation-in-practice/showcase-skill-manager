package skillmanagement.common.events

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder.bind
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder.durable
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

internal const val DEAD_LETTER_EXCHANGE = "$EXCHANGE_PREFIX.dead-letters"
internal const val DEAD_LETTER_QUEUE = "$QUEUE_PREFIX.dead-letters"
internal const val DEAD_LETTER_EXCHANGE_HEADER = "x-dead-letter-exchange"

@Configuration
@ComponentScan
class EventsConfiguration {

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory, objectMapper: ObjectMapper): RabbitTemplate =
        RabbitTemplate(connectionFactory).apply { messageConverter = Jackson2JsonMessageConverter(objectMapper) }

    @Bean
    fun jackson2JsonMessageConverter(objectMapper: ObjectMapper): MessageConverter =
        Jackson2JsonMessageConverter(objectMapper).apply { setClassMapper(DefaultJackson2JavaTypeMapper()) }

    @Bean
    fun eventExchange() = TopicExchange(EVENT_EXCHANGE)

    @Bean
    fun globalDeadLetterExchange(): FanoutExchange = FanoutExchange(DEAD_LETTER_EXCHANGE)

    @Bean
    fun globalDeadLetterQueue(): Queue = durable(DEAD_LETTER_QUEUE).build()

    @Bean
    fun globalDeadLetterBinding(): Binding = bind(globalDeadLetterQueue()).to(globalDeadLetterExchange())

}
