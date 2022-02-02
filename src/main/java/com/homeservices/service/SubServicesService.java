package com.homeservices.service;

import com.homeservices.data.entity.MainService;
import com.homeservices.data.entity.SubService;
import com.homeservices.data.repository.SubServiceRepository;
import com.homeservices.exception.FoundSubServiceException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public record SubServicesService(SubServiceRepository repository , MainServicesService mainServicesService)
{

    public SubService byName(final String name)
    {
        return repository.findByName(name);
    }

    public boolean addedNewSubService(final String name , final String des , final int price , final MainService mainService) throws FoundSubServiceException
    {
        SubService subService = byName(name);
        if (subService == null)
        {
            if (mainService != null)
            {
                SubService newSubService = new SubService();
                newSubService.setDescription(des);
                newSubService.setName(des);
                newSubService.setPrice(price);
                SubService save = repository.save(newSubService);

                Set<SubService> services = mainService.getServices();
                services.add(save);

                mainService.setServices(services);

                mainServicesService.repository().save(mainService);

                return true;
            }
            else throw new NullPointerException("main service is null");
        }
        else throw new FoundSubServiceException(name);
    }
}
