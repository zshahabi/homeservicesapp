package com.home.services.exception;

public class InvalidPostalCodeException extends Exception
{
    public InvalidPostalCodeException()
    {
        super("Please enter just a number");
    }
}
