package org.training.fundtransfer.service;

import org.training.fundtransfer.model.dto.FundTransferDto;
import org.training.fundtransfer.model.dto.request.FundTransferRequest;
import org.training.fundtransfer.model.dto.response.FundTransferResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FundTransferServiceImpl implements FundTransferService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public FundTransferServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public FundTransferResponse fundTransfer(FundTransferRequest fundTransferRequest) {
        String referenceId = UUID.randomUUID().toString();
        FundTransferDto transferDto = new FundTransferDto();
        transferDto.setReferenceId(referenceId);
        transferDto.setAccountId(fundTransferRequest.getAccountId());
        transferDto.setAmount(fundTransferRequest.getAmount());
        transferDto.setStatus("PENDING");

        rabbitTemplate.convertAndSend("fund.transfer.queue", transferDto);

        return new FundTransferResponse(referenceId, "Transfer initiated successfully");
    }

    @Override
    public FundTransferDto getTransferDetailsFromReferenceId(String referenceId) {
        // In a real implementation, this would query the database
        // For now, returning a mock response
        return new FundTransferDto(referenceId, "ACCOUNT123", 1000.0, "COMPLETED");
    }

    @Override
    public List<FundTransferDto> getAllTransfersByAccountId(String accountId) {
        // In a real implementation, this would query the database
        // For now, returning a mock list
        return List.of(
            new FundTransferDto(UUID.randomUUID().toString(), accountId, 500.0, "COMPLETED"),
            new FundTransferDto(UUID.randomUUID().toString(), accountId, 300.0, "PENDING")
        );
    }
}