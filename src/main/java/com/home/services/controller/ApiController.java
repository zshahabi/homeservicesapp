package com.home.services.controller;

import com.home.services.data.entity.SubService;
import com.home.services.data.entity.User;
import com.home.services.data.enums.Roles;
import com.home.services.dto.DTOSubService;
import com.home.services.dto.mapper.ShowExpertMapper;
import com.home.services.dto.mapper.SubServiceMapper;
import com.home.services.exception.FoundExpertOnThisSubServiceException;
import com.home.services.exception.NotFoundExpertOnThisSubServiceException;
import com.home.services.exception.NotFoundSubServiceException;
import com.home.services.exception.NotFoundUserException;
import com.home.services.service.CustomerService;
import com.home.services.service.ExpertService;
import com.home.services.service.SubServiceService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping(value = "/api", method = RequestMethod.GET)
public record ApiController(ExpertService expertService , CustomerService customerService ,
                            SubServiceService subServiceService , SubServiceMapper subServiceMapper ,
                            ShowExpertMapper showExpertMapper)
{
    @RequestMapping(value = "/sub-services")
    public List<DTOSubService> subServices()
    {
        final List<SubService> subServices = subServiceService.subServiceRepository().findAll();
        return subServiceMapper.toDtoSubServices(subServices);
    }

    @RequestMapping(value = "/show-experts/{SUB_SERVICE_ID}")
    public Map<String, Object> showExpertsSubService(@PathVariable(value = "SUB_SERVICE_ID") final String strSubServiceId)
    {
        final Map<String, Object> result = new HashMap<>();

        final long subServiceId = checkStrId(result , strSubServiceId , "Invalid sub service id");

        if (subServiceId > 0)
        {
            final List<User> expertsFindByRole = customerService.userRepository().findByRolesContainsAndSubServicesId(Roles.EXPERT , subServiceId);
            final Optional<SubService> subServiceFindById = subServiceService.subServiceRepository().findById(subServiceId);
            subServiceFindById.ifPresent(subService ->
            {
                result.put("subServiceName" , subService.getName());
                result.put("subServiceId" , subService.getId());
            });

            result.put("experts" , showExpertMapper.toDtoShowExperts(expertsFindByRole));
        }

        return result;
    }

    @RequestMapping(value = "/add-expert/{SUB_SERVICE_ID}")
    public Map<String, Object> addExpertSubService(@PathVariable(value = "SUB_SERVICE_ID") final String strSubServiceId , @RequestParam(value = "expertEmail") final String expertEmail)
    {
        final Map<String, Object> res = new HashMap<>();

        final long subServiceId = checkStrId(res , strSubServiceId , "Invalid sub service id");

        final AtomicBoolean result = new AtomicBoolean(false);

        if (subServiceId > 0)
        {
            final Optional<SubService> subServicesFindById = subServiceService.subServiceRepository().findById(subServiceId);

            subServicesFindById.ifPresent(subService ->
            {
                res.put("subServiceId" , subServiceId);
                res.put("subServiceName" , subService.getName());

                try
                {
                    result.set(expertService.addExpertSubService(subServiceId , expertEmail));
                }
                catch (NotFoundSubServiceException | NotFoundUserException | FoundExpertOnThisSubServiceException e)
                {
                    res.put("error" , e.getMessage());
                }

            });

        }

        res.put("result" , result.get());

        return res;
    }

    @RequestMapping(value = "/remove-expert/{SUB_SERVICE_ID}/{EXPERT_ID}")
    public Map<String, Object> removeExpert(@PathVariable(value = "SUB_SERVICE_ID") final String strSubServiceId , @PathVariable(value = "EXPERT_ID") final String strExpertId)
    {
        final Map<String, Object> res = new HashMap<>();

        final long subServiceId = checkStrId(res , strSubServiceId , "Invalid sub service id");
        final long expertId = checkStrId(res , strExpertId , "Invalid expert id");

        boolean result = false;

        if (subServiceId > 0 && expertId > 0)
        {
            try
            {
                result = expertService.removeExpertSubService(subServiceId , expertId);
            }
            catch (NotFoundSubServiceException | NotFoundUserException | NotFoundExpertOnThisSubServiceException e)
            {
                res.put("error" , e.getMessage());
            }
        }

        res.put("result" , result);
        res.put("operationName" , "Remove expert sub service");

        return res;
    }

    private long checkStrId(final Map<String, Object> modelMap , final String strId , final String errorMessage)
    {
        try
        {
            return Long.parseLong(strId);
        }
        catch (Exception e)
        {
            modelMap.put("error" , errorMessage);
        }
        return 0;
    }

}
