package com.homeservices.exception;

public class FoundSubServiceException extends Exception
{
    public FoundSubServiceException(String subServiceName)
    {
        super("This sub service name is exists: " + subServiceName);
    }
}
