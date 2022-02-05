package com.homeservices.service;

import com.homeservices.config.SpringConfig;
import com.homeservices.exception.FoundMainServiceException;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainServicesServiceTest
{

    private MainServicesService mainServicesService;

    @Before
    void setUp()
    {
        SpringConfig.config();
        mainServicesService = SpringConfig.newInstance(MainServicesService.class);
    }

    @Test
    void addCustomMainService()
    {
        try
        {
            boolean addCustomMainService = mainServicesService.addCustomMainService("THIS_IS_A_TEST_254");

            assertTrue(addCustomMainService);
        }
        catch (FoundMainServiceException e)
        {
            e.printStackTrace();
        }
    }
}