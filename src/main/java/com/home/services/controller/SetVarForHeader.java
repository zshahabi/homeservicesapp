package com.home.services.controller;

import com.home.services.data.enums.Roles;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import java.util.Collection;
import java.util.Locale;

@Component
public final class SetVarForHeader
{
    private static final String DEFAULT_BACK = "back";

    public void set(final ModelMap modelMap , final Authentication authentication)
    {
        set(modelMap , authentication , DEFAULT_BACK);
    }

    public void set(final ModelMap modelMap , final Authentication authentication , final String back)
    {
        if (authentication != null && authentication.isAuthenticated())
        {
            modelMap.put("hdrIsLogin" , true);

            final Roles role = getRole(authentication);

            assert role != null;
            modelMap.put("hdrRole" , role.name().toLowerCase(Locale.ROOT));
        }

        modelMap.put("hdrBack" , back);
    }

    // hamishe yedone role hast baraye har user , moghe register sabt shode
    private Roles getRole(final Authentication authentication)
    {
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (final GrantedAuthority authority : authorities) return Roles.valueOf(authority.getAuthority());

        return null;
    }
}
