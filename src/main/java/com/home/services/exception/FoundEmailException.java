package com.home.services.exception;

public class FoundEmailException extends Exception
{

    public FoundEmailException(final String email)
    {
        super("This email is found: " + email);
    }
}
