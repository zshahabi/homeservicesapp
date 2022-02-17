package com.home.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public final class DTOSearchExpert extends DTOSearchUser
{
    @JsonProperty("sub_service_name")
    private String subServiceName;
}
