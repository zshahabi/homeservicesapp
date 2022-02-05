package com.homeservices.exception;

public class NotFoundSubServiceException extends Exception
{
    public NotFoundSubServiceException(final String name)
    {
        super("Not found sub service: " + name);
    }

    public NotFoundSubServiceException(final long id)
    {
        super("Not found sub service: " + id);
    }
}
