package com.home.services.exception;

public class NotFoundOrderException extends Exception
{
    public NotFoundOrderException(final long orderId)
    {
        super("Not found order id: " + orderId);
    }
}
