package com.home.services.service;

import com.home.services.dto.DTORegister;
import com.home.services.exception.InvalidPasswordException;
import com.home.services.other.Str;
import org.springframework.stereotype.Component;

@Component
public final class CheckEmptyUserInfo
{
    public boolean check(final DTORegister user) throws NullPointerException, InvalidPasswordException
    {
        if (Str.notEmpty(user.getName()))
        {
            if (Str.notEmpty(user.getEmail()))
            {
                if (Str.notEmpty(user.getFamily()))
                {
                    if (Str.notEmpty(user.getPassword()))
                    {
                        if (user.getPassword().matches("^(?=.*\\\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$"))
                            return true;
                        else throw new InvalidPasswordException();
                    }
                    else throw new NullPointerException("password");
                }
                else throw new NullPointerException("family");
            }
            else throw new NullPointerException("email");
        }
        else throw new NullPointerException("name");
    }
}
