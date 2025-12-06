package com.TMS.Transport.Management.System.exception;

public class InsufficientCapacityException extends RuntimeException{
    public InsufficientCapacityException(String message){
        super(message);
    }
}
