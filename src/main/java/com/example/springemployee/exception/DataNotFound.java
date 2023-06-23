package com.example.springemployee.exception;

import java.util.function.Supplier;

public class DataNotFound extends RuntimeException  {

    public DataNotFound() {
        super("data do not exist");
    }

    public DataNotFound(String message) {
        super("Could not find any account with the email " + message);
    }

    public DataNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
