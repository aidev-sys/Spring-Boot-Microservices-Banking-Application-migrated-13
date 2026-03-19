package org.training.account.service.exception;

import org.training.common.exception.GlobalException;
import org.training.common.exception.GlobalErrorCode;

public class AccountClosingException extends GlobalException {

    public AccountClosingException(String errorMessage) {
        super(GlobalErrorCode.BAD_REQUEST, errorMessage);
    }
}