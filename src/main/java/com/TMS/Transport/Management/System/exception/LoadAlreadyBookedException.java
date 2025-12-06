package com.TMS.Transport.Management.System.exception;

public class LoadAlreadyBookedException extends RuntimeException{
    public LoadAlreadyBookedException(String message){
        super(message);
    }
}
