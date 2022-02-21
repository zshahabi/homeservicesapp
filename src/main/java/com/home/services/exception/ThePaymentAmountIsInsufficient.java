package com.home.services.exception;

public class ThePaymentAmountIsInsufficient extends Exception
{
    public ThePaymentAmountIsInsufficient(final long amountOfMoneyThatHasToBePaid , final int price)
    {
        super(String.format("Customer inventory is not enough: %d < %d" , price , amountOfMoneyThatHasToBePaid));
    }
}
