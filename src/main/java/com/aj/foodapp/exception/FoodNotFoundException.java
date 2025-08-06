package com.aj.foodapp.exception;

public class FoodNotFoundException extends RuntimeException
{
    public FoodNotFoundException(String errorMessage)
    {
        super(errorMessage);
    }

    public FoodNotFoundException(String errorMessage, Throwable cause)
    {
        super(errorMessage,cause);
    }
}
