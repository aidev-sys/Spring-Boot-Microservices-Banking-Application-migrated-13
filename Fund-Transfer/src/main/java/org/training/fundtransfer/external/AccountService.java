package org.training.fundtransfer.external;

import org.springframework.stereotype.Component;
import org.training.fundtransfer.model.dto.Account;
import org.training.fundtransfer.model.dto.response.Response;

@Component
public class AccountService {

    public Account readByAccountNumber(String accountNumber) {
        // Simulated Postgres database call
        return new Account();
    }

    public Response updateAccount(String accountNumber, Account account) {
        // Simulated Postgres database call
        return new Response();
    }
}