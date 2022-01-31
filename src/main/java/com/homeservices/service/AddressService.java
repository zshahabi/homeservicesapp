package com.homeservices.service;

import com.homeservices.data.repository.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public record AddressService(AddressRepository repository)
{
}
