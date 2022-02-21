package com.home.services.service;

import com.home.services.data.entity.MainService;
import com.home.services.data.entity.SubService;
import com.home.services.data.repository.SubServiceRepository;
import com.home.services.dto.DTOAddSubService;
import com.home.services.exception.FoundSubServiceException;
import com.home.services.exception.InvalidIdException;
import com.home.services.exception.InvalidPriceException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public record SubServiceService(SubServiceRepository subServiceRepository , MainServicesService mainServicesService)
{
    public SubService getSubServiceByName(final String subServiceName)
    {
        return subServiceRepository.findByName(subServiceName);
    }

    public boolean addNewSubService(final DTOAddSubService dtoAddSubService) throws InvalidIdException, InvalidPriceException, FoundSubServiceException
    {
        final SubService subService = getSubServiceByName(dtoAddSubService.getName());
        if (subService == null)
        {
            long mainServiceId;
            try
            {
                mainServiceId = Long.parseLong(dtoAddSubService.getMainServiceId());
                if (mainServiceId <= 0) throw new Exception();
            }
            catch (Exception e)
            {
                throw new InvalidIdException(dtoAddSubService.getMainServiceId());
            }

            int price;
            try
            {
                price = Integer.parseInt(dtoAddSubService.getPrice());
            }
            catch (Exception e)
            {
                throw new InvalidPriceException();
            }

            final MainService mainService = mainServicesService.getMainServiceById(mainServiceId);
            if (mainService != null)
            {
                SubService newSubService = new SubService();
                newSubService.setDescription(dtoAddSubService.getDescription());
                newSubService.setName(dtoAddSubService.getName());
                newSubService.setPrice(price);
                newSubService = subServiceRepository.save(newSubService);

                final Set<SubService> services = mainService.getServices();

                services.add(newSubService);

                mainService.setServices(services);

                mainServicesService.repository().save(mainService);

                return true;
            }
            else throw new NullPointerException("main service is null");
        }
        else throw new FoundSubServiceException(dtoAddSubService.getName());
    }

}
