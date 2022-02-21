package com.home.services.exception;

public final class InvalidIdException extends Exception
{
    public InvalidIdException(final String strId)
    {
        super("This is invalid: " + strId);
    }
}
