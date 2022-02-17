package com.home.services.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@RequiredArgsConstructor
public final class DTOExpertRegister extends DTORegister
{
    private MultipartFile image;

    @JsonIgnore
    private byte[] img;
}
