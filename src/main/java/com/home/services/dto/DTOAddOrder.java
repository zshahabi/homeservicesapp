package com.home.services.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DTOAddOrder extends DTOAddress
{
    private String name;
    private String description;

    @JsonProperty("subservice_name")
    private String subServiceName;

    @JsonProperty("email_customer")
    private String emailCustomer;

    @Override
    public String toString()
    {
        return "DTOAddOrder{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", subServiceName='" + subServiceName + '\'' +
                "} " + super.toString();
    }
}
