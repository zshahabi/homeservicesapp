package com.homeservices.service;

import com.homeservices.config.SpringConfig;
import com.homeservices.data.enums.UserStatus;
import com.homeservices.dto.DTOAddress;
import com.homeservices.dto.DTORegister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminServiceTest
{

    @BeforeEach
    void setUp()
    {
        SpringConfig.config();
    }

    @Test
    void register()
    {
        DTORegister dtoRegister = new DTORegister();
        dtoRegister.setName("REG NAME");
        dtoRegister.setFamily("REG FAMILY");
        dtoRegister.setEmail("REG@email.com");
        dtoRegister.setAccountCredit(10000);
        dtoRegister.setPassword("123456789");
        dtoRegister.setUserStatus(UserStatus.accepted.name());

        DTOAddress dtoAddress = new DTOAddress();
        dtoAddress.setAlley("REG ALLEY");
        dtoAddress.setStreet("REG STREET");
        dtoAddress.setPostalCode(1234566789);

        dtoRegister.setAddress(dtoAddress);

        boolean register = SpringConfig.newInstance(AdminService.class).register(dtoRegister);

        assertTrue(register);

    }
}