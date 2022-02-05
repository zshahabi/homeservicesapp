package com.homeservices.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DTOAddOrder
{
    private String name;
    private long customer;
    private String description;
    private DTOAddress address;
    private String subServiceName;
    private long expert;
}
