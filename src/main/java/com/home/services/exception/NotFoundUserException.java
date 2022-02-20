package com.home.services.exception;

public class NotFoundUserException extends Exception
{
    public NotFoundUserException(final String typeUser , final long id)
    {
        super("Not found " + typeUser + " id: " + id);
    }

    public NotFoundUserException(final String typeUser , final String email)
    {
        super("Not found " + typeUser + " email: " + email);
    }
}
