package com.homeservices.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public final class DTOAddOrderDescriptionCustomer extends DTOAddOrder
{
    private long price;
    private String description;
    private String timeDo;
}
