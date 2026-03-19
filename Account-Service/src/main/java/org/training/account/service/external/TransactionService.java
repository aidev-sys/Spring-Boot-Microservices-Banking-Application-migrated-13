package org.training.account.service.external;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.training.account.service.model.dto.external.TransactionResponse;

import java.util.List;

@Service
public class TransactionService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.queue.transactions}")
    private String transactionsQueue;

    public TransactionService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<TransactionResponse> getTransactionsFromAccountId(String accountId) {
        // Send message to RabbitMQ queue
        rabbitTemplate.convertAndSend(transactionsQueue, accountId);

        // Simulate receiving response (in real scenario, you'd have a proper response mechanism)
        return List.of();
    }
}