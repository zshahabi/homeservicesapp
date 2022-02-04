package com.homeservices.service;

import com.homeservices.data.entity.MainService;
import com.homeservices.data.repository.MainServiceRepository;
import com.homeservices.exception.FoundMainServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record MainServicesService(MainServiceRepository repository)
{

    public void addMainService()
    {
        final String[] mainServices =
                {
                        "Building decoration" ,
                        "Building Facilities" ,
                        "vehicles" ,
                        "Home Appliances" ,
                        "Cleaning and hygiene"
                };

        for (final String mainServiceName : mainServices)
        {
            try
            {
                final MainService mainService = new MainService();
                mainService.setName(mainServiceName);
                repository.save(mainService);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean addCustomMainService(final String name) throws FoundMainServiceException
    {
        final MainService byName = repository.findByName(name);
        if (byName == null)
        {
            final MainService mainService = new MainService();
            mainService.setName(name);
            repository.save(mainService);

            return true;
        }
        else throw new FoundMainServiceException(name);
    }

    public List<MainService> mainServices()
    {
        return repository.findAll();
    }
}
