package com.myexample.mydemo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class MyAuthException extends RuntimeException{
    public MyAuthException(String message){
        super(message);
    }
}
