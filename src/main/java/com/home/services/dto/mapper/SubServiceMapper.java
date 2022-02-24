package com.home.services.dto.mapper;

import com.home.services.data.entity.SubService;
import com.home.services.dto.DTOSubService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public final class SubServiceMapper
{
    public List<DTOSubService> toDtoSubServices(final List<SubService> subServices)
    {
        final List<DTOSubService> dtoSubServices = new ArrayList<>();

        if (subServices != null && subServices.size() > 0)
        {
            for (final SubService subService : subServices)
            {
                final DTOSubService dtoSubService = new DTOSubService();
                dtoSubService.setId(subService.getId());
                dtoSubService.setName(subService.getName());
                dtoSubService.setPrice(subService.getPrice());
                dtoSubService.setDescription(subService.getDescription());
                dtoSubService.setCreatedAt(subService.getCreatedAt().toString());

                dtoSubServices.add(dtoSubService);
            }
        }

        return dtoSubServices;
    }
}
