package org.training.user.service.external;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.training.user.service.model.external.Account;

@Service
public class AccountService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${account.service.queue.name}")
    private String queueName;

    public Account readByAccountNumber(String accountNumber) {
        // Send message to RabbitMQ queue
        rabbitTemplate.convertAndSend(queueName, accountNumber);
        
        // Simulate receiving response from RabbitMQ
        // In a real scenario, you would implement proper async handling or use a reply queue
        return new Account(); // Placeholder - actual implementation depends on your message contract
    }
}