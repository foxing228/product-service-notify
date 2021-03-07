package com.dima.productservice.exception;

public class NullEntityException extends RuntimeException {
    public NullEntityException(String message) {
        super(message);
    }
}
