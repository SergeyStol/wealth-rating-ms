package com.example.wealthratingms.common.exceptions.adapters;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Sergey Stol
 * 2021-12-18
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class HttpQuerySenderException extends RuntimeException{
    public HttpQuerySenderException(String message, Throwable cause) {
        super(message, cause);
    }
}