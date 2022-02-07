package com.homeservices.service;

import com.homeservices.config.SpringConfig;
import com.homeservices.data.enums.UserStatus;
import com.homeservices.dto.DTOAddress;
import com.homeservices.dto.DTORegister;
import com.homeservices.exception.NotFoundUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminServiceTest
{

    private AdminService adminService;

    @BeforeEach
    void setUp()
    {
        SpringConfig.config();
        adminService = SpringConfig.newInstance(AdminService.class);
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

        boolean register = adminService.register(dtoRegister);

        assertTrue(register);

    }

    @Test
    void changePassword()
    {
        try
        {
            boolean changePassword = adminService.changePassword(10 , "NEW_PASSWORD");

            assertTrue(changePassword);
        }
        catch (NotFoundUserException e)
        {
            e.printStackTrace();
        }
    }
}