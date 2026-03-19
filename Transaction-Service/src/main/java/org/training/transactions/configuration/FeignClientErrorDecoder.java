package org.training.transactions.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.training.transactions.exception.GlobalException;

import java.io.IOException;
import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<GlobalException> handleDataAccessException(DataAccessException ex) {
        log.error("Data access exception occurred", ex);
        GlobalException globalException = new GlobalException();
        globalException.setMessage("Database error occurred");
        globalException.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(globalException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<GlobalException> handleBadSqlGrammarException(BadSqlGrammarException ex) {
        log.error("SQL grammar error occurred", ex);
        GlobalException globalException = new GlobalException();
        globalException.setMessage("Invalid SQL query");
        globalException.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(globalException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<GlobalException> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        log.warn("No data found for the given request", ex);
        GlobalException globalException = new GlobalException();
        globalException.setMessage("No data found");
        globalException.setStatusCode(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(globalException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<GlobalException> handleSQLException(SQLException ex) {
        log.error("SQL exception occurred", ex);
        GlobalException globalException = new GlobalException();
        if (ex instanceof PSQLException psqlEx) {
            globalException.setMessage(psqlEx.getMessage());
        } else {
            globalException.setMessage("Database operation failed");
        }
        globalException.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(globalException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<GlobalException> handleIOException(IOException ex) {
        log.error("IO exception occurred while processing request", ex);
        GlobalException globalException = new GlobalException();
        globalException.setMessage("Error processing request");
        globalException.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(globalException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalException> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        GlobalException globalException = new GlobalException();
        globalException.setMessage("An unexpected error occurred");
        globalException.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(globalException, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}