package com.home.services.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public final class DTOAddSubService
{
    private String mainServiceId;

    private String name;
    private String price;
    private String description;

    @Override
    public String toString()
    {
        return "DTOAddSubService{" +
                "mainServiceId='" + mainServiceId + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
