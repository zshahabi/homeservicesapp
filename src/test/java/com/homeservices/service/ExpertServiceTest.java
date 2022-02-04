package com.homeservices.service;

import com.homeservices.config.SpringConfig;
import com.homeservices.data.entity.Experts;
import com.homeservices.data.enums.UserStatus;
import com.homeservices.dto.DTOAddress;
import com.homeservices.dto.DTOExpertRegister;
import com.homeservices.dto.DTORegister;
import com.homeservices.exception.ImageSizeException;
import com.homeservices.exception.NotFoundSubServiceException;
import com.homeservices.exception.NotFoundUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpertServiceTest
{

    private ExpertService expertService;

    @BeforeEach
    void setUp()
    {
        SpringConfig.config();
        expertService = SpringConfig.newInstance(ExpertService.class);
    }

    @Test
    void register()
    {
        DTOExpertRegister dtoRegister = new DTOExpertRegister();
        dtoRegister.setName("REG CUSTOMER NAME");
        dtoRegister.setFamily("REG CUSTOMER FAMILY");
        dtoRegister.setEmail("REGCUSTOMER@email.com");
        dtoRegister.setAccountCredit(10000);
        dtoRegister.setPassword("123456789");
        dtoRegister.setUserStatus(UserStatus.accepted.name());

        DTOAddress dtoAddress = new DTOAddress();
        dtoAddress.setAlley("REG CUSTOMER ALLEY");
        dtoAddress.setStreet("REG CUSTOMER STREET");
        dtoAddress.setPostalCode(1234566789);

        dtoRegister.setAddress(dtoAddress);

        dtoRegister.setImg(null);
        dtoRegister.setAreaOfExpertise("AreaOfExpertise");

        try
        {
            boolean register = expertService.register(dtoRegister);

            assertTrue(register);
        }
        catch (ImageSizeException e)
        {
            e.printStackTrace();
        }

    }

    @Test
    void changePassword()
    {
        try
        {
            boolean changePassword = expertService.changePassword(2 , "1215454");

            assertTrue(changePassword);
        }
        catch (NotFoundUserException e)
        {
            e.printStackTrace();
        }

    }

    @Test
    void addSubService()
    {
        try
        {
            boolean addSubService = expertService.addSubService(2 , 2);

            assertTrue(addSubService);
        }
        catch (NotFoundUserException | NotFoundSubServiceException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void getExpertByAreaOfExpertise()
    {
        List<Experts> areaOfExpertise = expertService.getExpertByAreaOfExpertise("AreaOfExpertise");
        assertNotNull(areaOfExpertise);
    }
}