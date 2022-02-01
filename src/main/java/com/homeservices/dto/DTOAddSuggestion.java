package com.homeservices.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DTOAddSuggestion
{
    private long expert;
    private long order;
    private String suggestion;
    private int price;
}
