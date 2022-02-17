package com.home.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.home.services.data.enums.UserType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DTORegister extends DTOAddress
{
    private String name;
    private String family;
    private String email;
    private String password;

    @JsonProperty("user_type")
    private UserType userType;

    private DTOAddress address;
}
