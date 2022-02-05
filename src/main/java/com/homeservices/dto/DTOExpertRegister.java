package com.homeservices.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@RequiredArgsConstructor
public class DTOExpertRegister extends DTORegister
{

    private byte[] img;

    @JsonProperty("area_of_expertise")
    private String areaOfExpertise;

}
