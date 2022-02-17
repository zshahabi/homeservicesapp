package com.home.services.other;

public final class Str
{
    private Str()
    {
    }

    public static boolean notEmpty(final String str)
    {
        return (str != null && !str.isEmpty());
    }
}
