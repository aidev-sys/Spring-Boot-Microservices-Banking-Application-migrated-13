package org.training.user.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String USER_CREATED_QUEUE = "user.created.queue";
    public static final String USER_CREATED_EXCHANGE = "user.created.exchange";
    public static final String USER_CREATED_ROUTING_KEY = "user.created";

    @Bean
    Queue userCreatedQueue() {
        return new Queue(USER_CREATED_QUEUE, false);
    }

    @Bean
    TopicExchange userCreatedExchange() {
        return new TopicExchange(USER_CREATED_EXCHANGE);
    }

    @Bean
    Binding userCreatedBinding() {
        return BindingBuilder.bind(userCreatedQueue()).to(userCreatedExchange()).with(USER_CREATED_ROUTING_KEY);
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