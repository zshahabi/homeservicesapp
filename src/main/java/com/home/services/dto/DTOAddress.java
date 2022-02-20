package com.home.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DTOAddress
{
    private String street;

    private String alley;

    @JsonProperty("postal_code")
    private String postalCode;

    @Override
    public String toString()
    {
        return "DTOAddress{" +
                "street='" + street + '\'' +
                ", alley='" + alley + '\'' +
                ", postalCode=" + postalCode +
                '}';
    }
}
