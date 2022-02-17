package com.home.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class DTOSearchUser
{
    private String name;
    private String family;
    private String email;

    @JsonProperty("user_status")
    private String userStatus;
}
