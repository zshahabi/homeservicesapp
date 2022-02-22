package com.home.services.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public final class DTOUsers extends DTOAddress
{
    private long id;
    private String name;
    private String family;
    private String email;
    private String password;
    private String status;
    private int accountCredit = 0;
    private String createdAt;

    private int rating;

    private String subService;

}
