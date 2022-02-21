package com.home.services.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public final class DTOShowSuggestion
{
    private long id;

    private String suggestion;

    private int price;

    // تاریخ انجام // hour
    private String timeDo;

    // hour
    private String startTime;

    private String expertEmail;
    private long expertId;
}
