package com.home.services.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DTOAddSuggestion
{
    private String suggestion;

    private int price;

    // تاریخ انجام // hour
    private int timeDo;

    // hour
    private int startTime;

    private long orderId;
}
