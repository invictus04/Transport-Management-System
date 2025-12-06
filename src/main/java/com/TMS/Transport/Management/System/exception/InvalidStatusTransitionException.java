package com.TMS.Transport.Management.System.exception;

public class InvalidStatusTransitionException extends  RuntimeException{
    public InvalidStatusTransitionException(String message){
        super(message);
    }
}
