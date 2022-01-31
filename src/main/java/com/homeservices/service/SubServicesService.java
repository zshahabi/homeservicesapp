package com.homeservices.service;

import com.homeservices.data.entity.SubService;
import com.homeservices.data.repository.SubServiceRepository;
import org.springframework.stereotype.Service;

@Service
public record SubServicesService(SubServiceRepository subServiceRepository)
{

    public SubService byName(final String name)
    {
        return subServiceRepository.findByName(name);
    }
}
