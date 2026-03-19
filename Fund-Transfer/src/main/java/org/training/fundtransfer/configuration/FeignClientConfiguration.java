package org.training.fundtransfer.configuration;

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

    public static final String FUND_TRANSFER_QUEUE = "fund.transfer.queue";
    public static final String FUND_TRANSFER_EXCHANGE = "fund.transfer.exchange";
    public static final String FUND_TRANSFER_ROUTING_KEY = "fund.transfer.routing.key";

    @Bean
    public Queue fundTransferQueue() {
        return new Queue(FUND_TRANSFER_QUEUE, false);
    }

    @Bean
    public DirectExchange fundTransferExchange() {
        return new DirectExchange(FUND_TRANSFER_EXCHANGE);
    }

    @Bean
    public Binding fundTransferBinding() {
        return BindingBuilder.bind(fundTransferQueue())
                .to(fundTransferExchange())
                .with(FUND_TRANSFER_ROUTING_KEY);
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