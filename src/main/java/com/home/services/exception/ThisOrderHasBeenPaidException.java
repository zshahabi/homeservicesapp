package com.home.services.exception;

public class ThisOrderHasBeenPaidException extends Exception
{
    public ThisOrderHasBeenPaidException(final String orderName)
    {
        super("This order has been paid: " + orderName);
    }
}
