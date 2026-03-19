package org.training.fundtransfer.external;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.training.fundtransfer.model.dto.Transaction;
import org.training.fundtransfer.model.dto.response.Response;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.queue.transaction}")
    private String transactionQueue;

    @Value("${rabbitmq.queue.internal-transactions}")
    private String internalTransactionsQueue;

    public Response makeTransaction(Transaction transaction) {
        rabbitTemplate.convertAndSend(transactionQueue, transaction);
        return new Response("Transaction initiated successfully");
    }

    public Response makeInternalTransactions(List<Transaction> transactions, String transactionReference) {
        InternalTransactionRequest request = new InternalTransactionRequest(transactions, transactionReference);
        rabbitTemplate.convertAndSend(internalTransactionsQueue, request);
        return new Response("Internal transactions initiated successfully");
    }

    private static class InternalTransactionRequest {
        private final List<Transaction> transactions;
        private final String transactionReference;

        public InternalTransactionRequest(List<Transaction> transactions, String transactionReference) {
            this.transactions = transactions;
            this.transactionReference = transactionReference;
        }

        public List<Transaction> getTransactions() {
            return transactions;
        }

        public String getTransactionReference() {
            return transactionReference;
        }
    }
}