package com.aj.foodapp.exception;

public class CustomFileStorageException extends RuntimeException
{
    public CustomFileStorageException(String errorMessage)
    {
        super(errorMessage);
    }

    public CustomFileStorageException(String errorMessage, Throwable cause)
    {
        super(errorMessage,cause);
    }
}
