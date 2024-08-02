package com.atipera.infrastructure.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(GithubResourceNotFoundException.class)
    private ResponseEntity<ErrorResponse> handle(GithubResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(404, ex.getMessage());
        log.warn("Couldn't find requested resource", ex);
        return new ResponseEntity<>(errorResponse, HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponse> handle(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "The server encountered an unexpected condition that prevented it from fulfilling the request");
        log.error("Error occurred while handling response", ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    record ErrorResponse(int responseCode, String message) {
    }
}
