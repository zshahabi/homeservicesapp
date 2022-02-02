package com.homeservices.service;

import com.homeservices.data.entity.MainService;
import com.homeservices.data.repository.MainServiceRepository;
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

    public List<MainService> mainServices()
    {
        return repository.findAll();
    }
}
