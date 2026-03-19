package org.training.account.service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    public static final String ACCOUNT_QUEUE = "account.queue";
    public static final String ACCOUNT_EXCHANGE = "account.exchange";
    public static final String ACCOUNT_ROUTING_KEY = "account.routing.key";

    @Bean
    Queue accountQueue() {
        return new Queue(ACCOUNT_QUEUE, false);
    }

    @Bean
    TopicExchange accountExchange() {
        return new TopicExchange(ACCOUNT_EXCHANGE);
    }

    @Bean
    Binding accountBinding() {
        return BindingBuilder.bind(accountQueue()).to(accountExchange()).with(ACCOUNT_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         MessageConverter jsonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter);
        return template;
    }
}