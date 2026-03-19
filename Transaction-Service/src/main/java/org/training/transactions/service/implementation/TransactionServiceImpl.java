package org.training.transactions.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.training.transactions.exception.AccountStatusException;
import org.training.transactions.exception.GlobalErrorCode;
import org.training.transactions.exception.InsufficientBalance;
import org.training.transactions.exception.ResourceNotFound;
import org.training.transactions.model.TransactionStatus;
import org.training.transactions.model.TransactionType;
import org.training.transactions.model.dto.TransactionDto;
import org.training.transactions.model.entity.Transaction;
import org.training.transactions.model.mapper.TransactionMapper;
import org.training.transactions.model.response.Response;
import org.training.transactions.model.response.TransactionRequest;
import org.training.transactions.repository.TransactionRepository;
import org.training.transactions.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final RabbitTemplate rabbitTemplate;
    private final TransactionMapper transactionMapper = new TransactionMapper();

    @Value("${spring.application.ok}")
    private String ok;

    @Override
    public Response addTransaction(TransactionDto transactionDto) {
        Transaction transaction = transactionMapper.convertToEntity(transactionDto);
        if (transactionDto.getTransactionType().equals(TransactionType.DEPOSIT.toString())) {
            transaction.setAmount(transactionDto.getAmount());
        } else if (transactionDto.getTransactionType().equals(TransactionType.WITHDRAWAL.toString())) {
            if (transactionDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                log.error("Invalid amount for withdrawal");
                throw new InsufficientBalance("Invalid amount for withdrawal");
            }
            transaction.setAmount(transactionDto.getAmount().negate());
        }

        transaction.setTransactionType(TransactionType.valueOf(transactionDto.getTransactionType()));
        transaction.setComments(transactionDto.getDescription());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setReferenceId(UUID.randomUUID().toString());

        transactionRepository.save(transaction);

        rabbitTemplate.convertAndSend("transaction.exchange", "transaction.created", transaction);

        return Response.builder()
                .message("Transaction completed successfully")
                .responseCode(ok).build();
    }

    @Override
    public Response internalTransaction(List<TransactionDto> transactionDtos, String transactionReference) {
        List<Transaction> transactions = transactionMapper.convertToEntityList(transactionDtos);

        transactions.forEach(transaction -> {
            transaction.setTransactionType(TransactionType.INTERNAL_TRANSFER);
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.setReferenceId(transactionReference);
        });

        transactionRepository.saveAll(transactions);

        transactions.forEach(transaction -> 
            rabbitTemplate.convertAndSend("transaction.exchange", "transaction.internal.completed", transaction));

        return Response.builder()
                .responseCode(ok)
                .message("Transaction completed successfully").build();
    }

    @Override
    public List<TransactionRequest> getTransaction(String accountId) {
        return transactionRepository.findTransactionByAccountId(accountId)
                .stream().map(transaction -> {
                    TransactionRequest transactionRequest = new TransactionRequest();
                    BeanUtils.copyProperties(transaction, transactionRequest);
                    transactionRequest.setTransactionStatus(transaction.getStatus().toString());
                    transactionRequest.setLocalDateTime(transaction.getTransactionDate());
                    transactionRequest.setTransactionType(transaction.getTransactionType().toString());
                    return transactionRequest;
                }).collect(Collectors.toList());
    }

    @Override
    public List<TransactionRequest> getTransactionByTransactionReference(String transactionReference) {
        return transactionRepository.findTransactionByReferenceId(transactionReference)
                .stream().map(transaction -> {
                    TransactionRequest transactionRequest = new TransactionRequest();
                    BeanUtils.copyProperties(transaction, transactionRequest);
                    transactionRequest.setTransactionStatus(transaction.getStatus().toString());
                    transactionRequest.setLocalDateTime(transaction.getTransactionDate());
                    transactionRequest.setTransactionType(transaction.getTransactionType().toString());
                    return transactionRequest;
                }).collect(Collectors.toList());
    }
}