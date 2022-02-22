package com.home.services.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public final class DTOComments
{
    private long id;
    private String user;
    private String text;
    private String createdAt;
}
