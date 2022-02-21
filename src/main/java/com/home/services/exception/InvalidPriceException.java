package com.home.services.exception;

public class InvalidPriceException extends Exception
{
    public InvalidPriceException()
    {
        super("Please enter just a number for price");
    }
}
