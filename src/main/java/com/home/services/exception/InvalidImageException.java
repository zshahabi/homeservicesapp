package com.home.services.exception;

public final class InvalidImageException extends Exception
{
    public InvalidImageException()
    {
        super("Image is invalid");
    }
}
