package com.TMS.Transport.Management.System.exception;

import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> buildResponse(String message, HttpStatus status){
        HashMap<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException exception){
        return buildResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({InvalidStatusTransitionException.class, InsufficientCapacityException.class})
    public ResponseEntity<Object> handleBusinessException(RuntimeException exception) {
        return buildResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({LoadAlreadyBookedException.class, OptimisticLockException.class})
    public ResponseEntity<Object> handleConcurrencyException(Exception exception) {
        String message = "The load was updated by another transaction. Please refresh and try again";
        if(exception instanceof LoadAlreadyBookedException) {
            message = exception.getMessage();
        }
        return buildResponse(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception e){
        return buildResponse("An Unexcepted error occured: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
