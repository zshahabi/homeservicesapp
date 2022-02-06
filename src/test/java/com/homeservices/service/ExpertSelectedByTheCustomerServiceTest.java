package com.homeservices.service;

import com.homeservices.config.SpringConfig;
import com.homeservices.data.entity.ExpertSelectedByTheCustomer;
import com.homeservices.exception.FoundSelectedByTheCustomerException;
import com.homeservices.exception.NotFoundException;
import com.homeservices.exception.NotFoundUserException;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpertSelectedByTheCustomerServiceTest
{

    private ExpertSelectedByTheCustomerService expertSelectedByTheCustomerService;

    @Before
    void setUp()
    {
        SpringConfig.config();
        expertSelectedByTheCustomerService = SpringConfig.newInstance(ExpertSelectedByTheCustomerService.class);
    }

    @Test
    void addSelected()
    {
        try
        {
            boolean addSelected = expertSelectedByTheCustomerService.addSelected(1 , 2);

            assertTrue(addSelected);
        }
        catch (NotFoundUserException | FoundSelectedByTheCustomerException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void getSelected()
    {
        try
        {
            List<ExpertSelectedByTheCustomer> getSelected = expertSelectedByTheCustomerService.getSelected(10);

            assertNotNull(getSelected);
        }
        catch (NotFoundUserException | NotFoundException e)
        {
            e.printStackTrace();
        }
    }
}