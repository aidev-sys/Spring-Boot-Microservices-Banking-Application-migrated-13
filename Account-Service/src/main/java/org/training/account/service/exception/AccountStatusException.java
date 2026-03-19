package org.training.account.service.exception;

import org.training.common.exception.GlobalErrorCode;
import org.training.common.exception.GlobalException;

public class AccountStatusException extends GlobalException {
    public AccountStatusException(String errorMessage) {
        super(errorMessage, GlobalErrorCode.BAD_REQUEST);
    }
}