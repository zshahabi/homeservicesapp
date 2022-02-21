package com.home.services.exception;

public final class InvalidPasswordException extends Exception
{
    public InvalidPasswordException()
    {
        super("This password is invalid");
    }
}
