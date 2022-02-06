package com.homeservices.service;

import com.homeservices.config.SpringConfig;
import com.homeservices.data.entity.Customer;
import com.homeservices.data.enums.UserStatus;
import com.homeservices.dto.DTOAddress;
import com.homeservices.dto.DTORegister;
import com.homeservices.exception.NotFoundUserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest
{

    private CustomerService customerService;

    @BeforeEach
    void setUp()
    {
        SpringConfig.config();
        customerService = SpringConfig.newInstance(CustomerService.class);
    }

    @Test
    void register()
    {
        DTORegister dtoRegister = new DTORegister();
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

        boolean register = customerService.register(dtoRegister);

        assertTrue(register);
    }

    @Test
    void getAllCustomer()
    {
        List<Customer> allCustomer = customerService.getAllCustomer();
        assertNotNull(allCustomer);
    }

    @Test
    void getUserById()
    {
        Customer userById = customerService.getUserById(1);
        assertNotNull(userById);
    }

    @Test
    void getUserByEmailAndPassword()
    {
        Customer customer = customerService.getUserByUEmailAndPassword("REGCUSTOMER@email.com" , "123456789");
        assertNotNull(customer);
    }

    @Test
    void getUserByStatus()
    {
        List<Customer> customer = customerService.getUserByStatus(UserStatus.accepted);
        assertNotNull(customer);
    }

    @Test
    void changePassword()
    {
        try
        {
            boolean changePassword = customerService.changePassword(12 , "NEW_PASSWORD");

            assertTrue(changePassword);
        }
        catch (NotFoundUserException e)
        {
            e.printStackTrace();
        }
    }
}