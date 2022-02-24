package com.home.services.exception;

public class NotFoundExpertOnThisSubServiceException extends Exception
{
    public NotFoundExpertOnThisSubServiceException(final String email , final String subServiceName)
    {
        super(String.format("Not Found expert[%s] on this sub service[%s]" , email , subServiceName));
    }
}
