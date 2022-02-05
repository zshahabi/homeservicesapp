package com.homeservices.exception;

public class FoundMainServiceException extends Exception
{
    public FoundMainServiceException(String mainServiceName)
    {
        super("This main service is exists: " + mainServiceName);
    }
}
