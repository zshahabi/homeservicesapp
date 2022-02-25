package com.home.services.service;

import com.home.services.data.entity.UserRegisterValidationCode;
import com.home.services.data.repository.UserRegisterValidationCodeRepository;
import org.springframework.stereotype.Service;

@Service
public record UserRegisterValidationCodeService(
        UserRegisterValidationCodeRepository userRegisterValidationCodeRepository)
{


    public UserRegisterValidationCode getValidationCode(final long code , final long userId)
    {
        return userRegisterValidationCodeRepository.getUserRegisterValidationCode(code , userId);
    }
}
