package com.home.services.controller;

import com.home.services.data.entity.SubService;
import com.home.services.data.entity.User;
import com.home.services.data.enums.Roles;
import com.home.services.data.enums.UserStatus;
import com.home.services.data.repository.UserRepository;
import com.home.services.dto.DTOSearchUser;
import com.home.services.dto.DTOSubService;
import com.home.services.dto.mapper.ShowExpertMapper;
import com.home.services.dto.mapper.SubServiceMapper;
import com.home.services.dto.mapper.UsersMapper;
import com.home.services.exception.FoundExpertOnThisSubServiceException;
import com.home.services.exception.InvalidUserStatusException;
import com.home.services.exception.NotFoundExpertOnThisSubServiceException;
import com.home.services.exception.NotFoundSubServiceException;
import com.home.services.exception.NotFoundUserException;
import com.home.services.other.Str;
import com.home.services.service.CustomerService;
import com.home.services.service.ExpertService;
import com.home.services.service.SubServiceService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping(value = "/api", method = RequestMethod.GET)
public record ApiController(ExpertService expertService , CustomerService customerService ,
                            SubServiceService subServiceService , SubServiceMapper subServiceMapper ,
                            ShowExpertMapper showExpertMapper , UsersMapper usersMapper)
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

    private Roles checkRole(final Map<String, Object> modelMap , final String roleStr)
    {
        try
        {
            if (Str.notEmpty(roleStr) && !roleStr.toUpperCase(Locale.ROOT).equals("ADMIN"))
                return Roles.valueOf(roleStr.toUpperCase(Locale.ROOT));
            else throw new Exception();
        }
        catch (Exception e)
        {
            modelMap.put("error" , "Invalid user type");
        }

        return null;
    }

    @RequestMapping(value = "/users/{ROLE}")
    public Map<String, Object> users(@PathVariable(value = "ROLE") final String roleStr)
    {
        final Map<String, Object> res = new HashMap<>();

        final Roles role = checkRole(res , roleStr);
        if (role != null)
        {
            res.put("role" , role.name().toLowerCase(Locale.ROOT));

            List<User> users = null;

            if (role.equals(Roles.EXPERT)) users = expertService.expertRepository().findByRolesContains(Roles.EXPERT);
            else if (role.equals(Roles.CUSTOMER))
                users = customerService.userRepository().findByRolesContains(Roles.CUSTOMER);
            else res.put("error" , "Invalid role");

            if (users != null && users.size() > 0) res.put("users" , usersMapper.toDtoUsers(users));
        }

        return res;
    }

    @RequestMapping(value = "/accept-user/{USER_ID}")
    public Map<String, Object> acceptUser(@PathVariable(value = "USER_ID") final String strUserId)
    {
        final Map<String, Object> res = new HashMap<>();

        final long userId = checkStrId(res , strUserId , "Invalid user id");

        boolean result = false;
        if (userId > 0)
        {
            final UserRepository userRepository = customerService.userRepository();

            final User userFindById = userRepository.findById(userId);

            if (userFindById != null)
            {
                if (!userFindById.getUserStatus().equals(UserStatus.ACCEPTED))
                    result = userRepository.changeUserStatus(UserStatus.ACCEPTED , userId) > 0;
                else res.put("error" , "This user is accepted");
            }
            else res.put("error" , "Invalid user id");
        }

        res.put("result" , result);

        res.put("operationName" , "Accept user");

        return res;
    }

    @RequestMapping(value = "/remove-user/{USER_ID}")
    public Map<String, Object> removeUser(@PathVariable(value = "USER_ID") final String strUserId)
    {
        final Map<String, Object> res = new HashMap<>();

        final long userId = checkStrId(res , strUserId , "Invalid user id");

        boolean result = false;
        if (userId > 0)
        {
            final UserRepository userRepository = customerService.userRepository();

            final User userFindById = userRepository.findById(userId);

            if (userFindById != null)
            {
                userRepository.delete(userFindById);
                result = true;
            }
            else res.put("error" , "Invalid user id");
        }

        res.put("result" , result);

        res.put("operationName" , "Remove user");

        return res;
    }

    @RequestMapping(value = "/users/{ROLE}/search")
    public Map<String, Object> searchUsers(@PathVariable(value = "ROLE") final String strRole , @ModelAttribute("searchUsers") final DTOSearchUser dtoSearchUser)
    {
        final Map<String, Object> res = new HashMap<>();

        res.put("role" , strRole);

        final Roles role = checkRole(res , strRole);

        if (role != null)
        {
            List<User> users = null;

            dtoSearchUser.setUserStatus(dtoSearchUser.getUserStatus().toUpperCase(Locale.ROOT).replace(" " , "_"));

            if (role.equals(Roles.EXPERT))
            {
                try
                {
                    users = expertService.searchExperts(dtoSearchUser);
                }
                catch (InvalidUserStatusException | NullPointerException e)
                {
                    res.put("error" , e.getMessage());
                }
            }
            else if (role.equals(Roles.CUSTOMER))
            {
                try
                {
                    users = customerService.searchCustomer(dtoSearchUser);
                }
                catch (InvalidUserStatusException e)
                {
                    res.put("error" , e.getMessage());
                }
            }
            else res.put("error" , "Invalid role");


            if (users != null)
            {
                res.put("users" , usersMapper.toDtoUsers(users));
                return res;
            }
        }

        return res;
    }

}
