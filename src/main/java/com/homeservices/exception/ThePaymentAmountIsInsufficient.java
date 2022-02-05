package com.homeservices.exception;

public class ThePaymentAmountIsInsufficient extends Exception
{
    public ThePaymentAmountIsInsufficient(final long amountOfMoneyThatHasToBePaid , final int price)
    {
        super(String.format("%d < %d" , price , amountOfMoneyThatHasToBePaid));
    }
}
