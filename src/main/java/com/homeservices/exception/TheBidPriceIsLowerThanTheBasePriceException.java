package com.homeservices.exception;

public final class TheBidPriceIsLowerThanTheBasePriceException extends Exception
{
    public TheBidPriceIsLowerThanTheBasePriceException(final int basePrice , final int suggestionPrice)
    {
        super(String.format("Base price [%d] > Suggestion price [%d]" , basePrice , suggestionPrice));
    }
}
