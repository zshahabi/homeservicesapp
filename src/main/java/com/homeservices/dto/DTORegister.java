package com.homeservices.dto;

import com.homeservices.data.entity.Address;
import com.homeservices.data.entity.UserStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DTORegister
{
    private String name;
    private String family;
    private String email;
    private String password;
    private String userStatus;
    private int accountCredit = 0;
    private DTOAddress address;
}
