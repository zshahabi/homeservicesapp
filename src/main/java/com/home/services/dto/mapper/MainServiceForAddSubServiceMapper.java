package com.home.services.dto.mapper;

import com.home.services.data.entity.MainService;
import com.home.services.dto.DTOMainServiceForAddSubService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MainServiceForAddSubServiceMapper
{
    public List<DTOMainServiceForAddSubService> toDtoMainServiceForAddSubServices(final List<MainService> mainServices)
    {
        if (mainServices.size() > 0)
        {
            final List<DTOMainServiceForAddSubService> dtoMainServiceForAddSubServices = new ArrayList<>();
            for (final MainService mainService : mainServices)
            {
                final DTOMainServiceForAddSubService dtoMainServiceForAddSubService = new DTOMainServiceForAddSubService();
                dtoMainServiceForAddSubService.setId(mainService.getId());
                dtoMainServiceForAddSubService.setName(mainService.getName());
                dtoMainServiceForAddSubServices.add(dtoMainServiceForAddSubService);
            }
            return dtoMainServiceForAddSubServices;
        }

        return null;
    }
}
