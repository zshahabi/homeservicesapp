package com.homeservices.exception;

public class NotFoundUserException extends Exception
{
    public NotFoundUserException(final String typeUser , final long id)
    {
        super("Not found " + typeUser + " id: " + id);
    }
}
