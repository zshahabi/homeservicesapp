package com.home.services.data.enums;

import java.util.Locale;

public enum UserStatus
{
    NEW_USER, WAITING_ACCEPT, ACCEPTED;

    public static UserStatus nameToUserStatus(final String name)
    {
        try
        {
            return valueOf(name.toUpperCase(Locale.ROOT));
        }
        catch (Exception ignored)
        {
        }
        return null;
    }
}
