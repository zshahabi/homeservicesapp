package com.home.services.exception;

public final class FoundExpertOnThisSubServiceException extends Exception
{
    public FoundExpertOnThisSubServiceException(final String email , final String subServiceName)
    {
        super(String.format("Found expert[%s] on this sub service[%s]" , email , subServiceName));
    }
}
