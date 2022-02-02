package com.homeservices.service;

import com.homeservices.config.SpringConfig;
import com.homeservices.data.entity.Address;
import com.homeservices.dto.DTOAddress;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class AddressServiceTest
{

    @Test
    void addAddress()
    {
        SpringConfig.config();

        AddressService addressService = SpringConfig.newInstance(AddressService.class);

        final DTOAddress dtoAddress = new DTOAddress();
        dtoAddress.setStreet("STREET");
        dtoAddress.setAlley("ALLEY");
        dtoAddress.setPostalCode(123456789);

        Address address = addressService.addAddress(dtoAddress);

        assertNotNull(address);
    }
}