package com.home.services.exception;

public final class InvalidUserStatusException extends Exception
{
    public InvalidUserStatusException(String userStatus)
    {
        super("This user status is invalid: " + userStatus);
    }
}
