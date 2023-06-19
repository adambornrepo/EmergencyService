package com.tech.exception.custom;

public class DirectoryCreationException extends RuntimeException{
    public DirectoryCreationException(String message) {
        super(message);
    }

    public DirectoryCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
