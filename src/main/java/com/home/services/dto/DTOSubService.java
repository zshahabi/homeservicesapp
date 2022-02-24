package com.home.services.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public final class DTOSubService
{
    private long id;
    private String name;
    private int price;
    private String description;
    private String createdAt;
}
