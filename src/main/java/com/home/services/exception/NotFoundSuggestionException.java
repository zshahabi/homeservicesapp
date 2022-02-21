package com.home.services.exception;

public class NotFoundSuggestionException extends Exception
{
    public NotFoundSuggestionException(final long suggestionId)
    {
        super("This suggestion not found: " + suggestionId);
    }

    public NotFoundSuggestionException()
    {
        super("Not found suggestion");
    }
}
