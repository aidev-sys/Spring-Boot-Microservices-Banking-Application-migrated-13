package org.training.account.service.exception;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ACCOUNT_CREATED_QUEUE = "account.created.queue";
    public static final String ACCOUNT_UPDATED_QUEUE = "account.updated.queue";
    public static final String ACCOUNT_DELETED_QUEUE = "account.deleted.queue";

    @Bean
    public Queue accountCreatedQueue() {
        return new Queue(ACCOUNT_CREATED_QUEUE, false);
    }

    @Bean
    public Queue accountUpdatedQueue() {
        return new Queue(ACCOUNT_UPDATED_QUEUE, false);
    }

    @Bean
    public Queue accountDeletedQueue() {
        return new Queue(ACCOUNT_DELETED_QUEUE, false);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }
}