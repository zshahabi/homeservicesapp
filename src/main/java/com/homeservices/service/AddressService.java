package com.homeservices.service;

import com.homeservices.data.entity.Address;
import com.homeservices.data.repository.AddressRepository;
import com.homeservices.dto.DTOAddress;
import org.springframework.stereotype.Service;

@Service
public record AddressService(AddressRepository repository)
{

    public Address addAddress(DTOAddress dtoAddress)
    {
        Address address = new Address();

        address.setAlley(dtoAddress.getAlley());
        address.setStreet(dtoAddress.getStreet());
        address.setPostalCode(dtoAddress.getPostalCode());

        return repository.save(address);
    }
}
