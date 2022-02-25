package com.home.services.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DTOPayment
{
    private String cardNumber;
    private String cvv2;
    private String date;
}
