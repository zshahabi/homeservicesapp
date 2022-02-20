package com.home.services.dto.mapper;

import com.home.services.data.entity.Address;
import com.home.services.dto.DTOAddress;
import com.home.services.exception.InvalidPostalCodeException;
import org.springframework.stereotype.Component;

@Component
public final class AddressMapper
{
    public Address toAddress(final DTOAddress dtoAddress) throws InvalidPostalCodeException
    {
        final Address address = new Address();
        address.setAlley(dtoAddress.getAlley());
        try
        {
            address.setPostalCode(Integer.parseInt(dtoAddress.getPostalCode()));
        }
        catch (Exception e)
        {
            throw new InvalidPostalCodeException();
        }
        address.setStreet(dtoAddress.getStreet());
        return address;
    }
}
