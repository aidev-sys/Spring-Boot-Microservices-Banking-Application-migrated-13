package org.training.transactions.external;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.training.transactions.model.external.Account;
import org.training.transactions.model.response.Response;

@Service
public class AccountService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${account.service.queue.name}")
    private String accountQueueName;

    public ResponseEntity<Account> readByAccountNumber(String accountNumber) {
        Account account = (Account) rabbitTemplate.receiveAndConvert(accountQueueName + ".read." + accountNumber);
        return ResponseEntity.ok(account);
    }

    public ResponseEntity<Response> updateAccount(String accountNumber, Account account) {
        rabbitTemplate.convertAndSend(accountQueueName + ".update." + accountNumber, account);
        return ResponseEntity.ok(new Response("Account updated successfully"));
    }
}