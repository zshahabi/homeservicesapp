package com.home.services.dto.mapper;

import com.home.services.data.entity.Address;
import com.home.services.dto.DTOAddress;
import org.springframework.stereotype.Component;

@Component
public final class AddressMapper
{
    public Address toAddress(final DTOAddress dtoAddress)
    {
        final Address address = new Address();
        dtoAddress.setAlley(dtoAddress.getAlley());
        dtoAddress.setPostalCode(dtoAddress.getPostalCode());
        dtoAddress.setStreet(dtoAddress.getStreet());
        return address;
    }
}
