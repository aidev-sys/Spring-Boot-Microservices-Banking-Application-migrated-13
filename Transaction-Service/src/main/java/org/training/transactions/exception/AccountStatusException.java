package org.training.transactions.exception;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@ConditionalOnProperty(name = "transaction.service.type", havingValue = "rabbitmq")
public class TransactionService {

    private static final String TRANSACTION_QUEUE = "transaction.queue";
    private static final String TRANSACTION_EXCHANGE = "transaction.exchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = TRANSACTION_QUEUE)
    public void handleTransaction(TransactionMessage message) {
        // Process transaction logic here
        processTransaction(message);
    }

    public CompletableFuture<Void> sendTransactionAsync(TransactionMessage message) {
        return CompletableFuture.runAsync(() -> rabbitTemplate.convertAndSend(TRANSACTION_EXCHANGE, message));
    }

    private void processTransaction(TransactionMessage message) {
        // Implementation for processing transaction
    }

    @Configuration
    public static class RabbitConfig {

        @Bean
        public Queue transactionQueue() {
            return new Queue(TRANSACTION_QUEUE, false);
        }
    }
}

class TransactionMessage {
    private String id;
    private String type;
    private Double amount;
    private String accountId;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}

package org.training.transactions.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AccountStatusException extends RuntimeException {

    public AccountStatusException(String message) {
        super(message);
    }
}