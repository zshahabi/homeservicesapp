package com.homeservices.service;

import com.homeservices.config.SpringConfig;
import com.homeservices.data.entity.SubService;
import com.homeservices.exception.FoundSubServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubServicesServiceTest
{

    private SubServicesService subServicesService;

    @BeforeEach
    void setUp()
    {
        SpringConfig.config();
        subServicesService = SpringConfig.newInstance(SubServicesService.class);
    }

    @Test
    void byName()
    {
        SubService subService = subServicesService.byName("sub_service");

        assertNull(subService);
    }

    @Test
    void addedNewSubService()
    {
        try
        {
            boolean addedNewSubService = subServicesService.addedNewSubService("subservice" , "DES" , 454545 , subServicesService.mainServicesService().repository().getById(2L));

            assertTrue(addedNewSubService);
        }
        catch (FoundSubServiceException | NullPointerException e)
        {
            e.printStackTrace();
        }
    }
}