package com.home.services.service;

import com.home.services.data.entity.SubService;
import com.home.services.data.repository.SubServiceRepository;
import org.springframework.stereotype.Service;

@Service
public record SubServiceService(SubServiceRepository subServiceRepository)
{
    public SubService getSubServiceByName(final String subServiceName)
    {
        return subServiceRepository.findByName(subServiceName);
    }

}
