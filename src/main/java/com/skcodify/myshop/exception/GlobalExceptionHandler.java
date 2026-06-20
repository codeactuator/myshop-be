package com.skcodify.myshop.exception;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(EntityNotFoundException ex) {
        log.error("Entity not found: ", ex);
        String message = ex.getMessage() != null ? ex.getMessage() : "Resource not found";
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", message));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        log.error("Runtime exception occurred: ", ex);
        String message = ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", message));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(Map.of("error", "The uploaded file is too large! Please upload an image smaller than 10MB."));
    }
}