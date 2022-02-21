package com.home.services.exception;

public class FoundMainServiceException extends Exception
{
    public FoundMainServiceException(final String mainServiceName)
    {
        super("This main service is exists: " + mainServiceName);
    }
}
