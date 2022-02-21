package com.home.services.controller;

import com.home.services.data.entity.Order;
import com.home.services.data.entity.Suggestion;
import com.home.services.data.enums.UserType;
import com.home.services.dto.DTOAddOrder;
import com.home.services.dto.DTOAddSubService;
import com.home.services.dto.DTOAddSuggestion;
import com.home.services.dto.DTOCustomerRegister;
import com.home.services.dto.DTOExpertRegister;
import com.home.services.dto.mapper.MainServiceForAddSubServiceMapper;
import com.home.services.dto.mapper.ShowSuggestionMapper;
import com.home.services.exception.FoundEmailException;
import com.home.services.exception.FoundMainServiceException;
import com.home.services.exception.FoundSubServiceException;
import com.home.services.exception.ImageSizeException;
import com.home.services.exception.InvalidIdException;
import com.home.services.exception.InvalidImageException;
import com.home.services.exception.InvalidPasswordException;
import com.home.services.exception.InvalidPostalCodeException;
import com.home.services.exception.InvalidPriceException;
import com.home.services.exception.NotFoundOrderException;
import com.home.services.exception.NotFoundSubServiceException;
import com.home.services.exception.NotFoundSuggestionException;
import com.home.services.exception.NotFoundUserException;
import com.home.services.exception.ThePaymentAmountIsInsufficient;
import com.home.services.exception.ThisOrderHasBeenPaidException;
import com.home.services.other.Str;
import com.home.services.service.CustomerService;
import com.home.services.service.ExpertService;
import com.home.services.service.MainServicesService;
import com.home.services.service.OrderService;
import com.home.services.service.SubServiceService;
import com.home.services.service.SuggestionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping(value = "/")
public record Views(OrderService orderService , SubServiceService subServiceService ,
                    SuggestionService suggestionService , ShowSuggestionMapper showSuggestionMapper ,
                    MainServicesService mainServicesService ,
                    MainServiceForAddSubServiceMapper mainServiceForAddSubServiceMapper , ExpertService expertService ,
                    CustomerService customerService)
{
    @RequestMapping("/login")
    public String login()
    {
        return "login";
    }

    @RequestMapping(value = "/service-view")
    @RolesAllowed("ADMIN")
    public String serviceView(final ModelMap model)
    {
        final List<Order> orders = orderService.orderRepository().findAll();
        model.put("orders" , orders);

        return "services";
    }

    @RequestMapping(value = "/add-new-order", method = RequestMethod.GET)
    @RolesAllowed({"ADMIN" , "EXPERT"})
    public String addNewOrderView(final ModelMap model)
    {
        model.put("subServiceNames" , getSubServiceNames());
        return "add-new-order";
    }

    @RequestMapping(value = "/add-new-order", method = RequestMethod.POST)
    @RolesAllowed({"ADMIN" , "EXPERT"})
    public String addNewOrder(final ModelMap model , @ModelAttribute("dtoAddNewOrder") DTOAddOrder dtoAddOrder)
    {
        boolean addOrder = false;
        try
        {
            addOrder = orderService.addOrder(dtoAddOrder);
        }
        catch (NotFoundUserException | NotFoundSubServiceException | InvalidPostalCodeException e)
        {
            model.put("error" , e.getMessage());
        }

        model.put("result" , addOrder);

        model.put("subServiceNames" , getSubServiceNames());
        return "add-new-order";
    }

    private List<String> getSubServiceNames()
    {
        return subServiceService.subServiceRepository().getSubServiceByNames();
    }

    @RequestMapping(value = {"/add-suggestion" , "/add-suggestion/{ORDER_ID}"}, method = RequestMethod.GET)
    @RolesAllowed("EXPERT")
    public String addSuggestion(final ModelMap modelMap , @PathVariable(value = "ORDER_ID") final String strOrderId)
    {
        long orderId = 0;
        try
        {
            orderId = Long.parseLong(strOrderId);
        }
        catch (Exception e)
        {
            modelMap.put("error" , "Invalid order id");
        }

        if (orderId > 0)
        {
            modelMap.put("orderId" , orderId);

            final Optional<Order> orderFindById = orderService.orderRepository().findById(orderId);
            orderFindById.ifPresentOrElse(order -> modelMap.put("orderName" , order.getName()) , () -> modelMap.put("error" , "Not found order"));
        }

        return "add-suggestion";
    }

    @RequestMapping(value = {"/add-suggestion" , "/add-suggestion/{ORDER_ID}"}, method = RequestMethod.POST)
    @RolesAllowed("EXPERT")
    public String addSuggestion(final ModelMap modelMap , @PathVariable(value = "ORDER_ID") final String strOrderId , final Authentication authentication , @ModelAttribute("addSuggestion") final DTOAddSuggestion dtoAddSuggestion)
    {
        long orderId = 0;
        try
        {
            orderId = Long.parseLong(strOrderId);
        }
        catch (Exception e)
        {
            modelMap.put("error" , "Invalid order id");
        }

        dtoAddSuggestion.setOrderId(orderId);

        (orderService.orderRepository().findById(orderId)).ifPresent(order -> modelMap.put("orderName" , order.getName()));

        boolean result = false;
        try
        {
            result = suggestionService.addSuggestion(dtoAddSuggestion , suggestionService.customerRepository().findByEmail(authentication.getName()));
        }
        catch (NotFoundOrderException e)
        {
            modelMap.put("error" , e.getMessage());
        }

        modelMap.put("result" , result);
        return "add-suggestion";
    }

    @RequestMapping(value = {"/show-suggestion" , "/show-suggestion/{ORDER_ID}"}, method = RequestMethod.GET)
    @RolesAllowed({"ADMIN" , "EXPERT"})
    public String showSuggestionOrder(final ModelMap modelMap , @PathVariable(value = "ORDER_ID") final String strOrderId)
    {
        final long orderId = checkStrId(modelMap , strOrderId , "Invalid order id");

        if (orderId > 0)
        {
            (orderService.orderRepository().findById(orderId)).ifPresent(order ->
            {
                modelMap.put("orderName" , order.getName());
                modelMap.put("orderId" , orderId);
            });

            final List<Suggestion> suggestionOrder = suggestionService.suggestionRepository().findByOrderId(orderId);

            if (suggestionOrder.size() > 0)
                modelMap.put("showSuggestions" , showSuggestionMapper.toDtoShowSuggestion(suggestionOrder));
            else modelMap.put("error" , "Not found suggestion");
        }

        return "show-suggestion";
    }

    @RequestMapping(value = {"/remove-suggestion" , "/remove-suggestion/{EXPERT_ID}/{SUGGESTION_ID}"}, method = RequestMethod.GET)
    @RolesAllowed({"ADMIN" , "EXPERT"})
    public String removeSuggestion(final ModelMap modelMap , @PathVariable(value = "EXPERT_ID") final String strExpertId , @PathVariable(value = "SUGGESTION_ID") final String strSuggestionId)
    {
        final long expertId = checkStrId(modelMap , strExpertId , "Invalid expert id");
        final long suggestionId = checkStrId(modelMap , strSuggestionId , "Invalid suggestion id");

        boolean result = false;
        if (expertId > 0 && suggestionId > 0)
        {
            try
            {
                result = suggestionService.removeSuggestion(expertId , suggestionId);
            }
            catch (NotFoundSuggestionException | NotFoundUserException e)
            {
                modelMap.put("error" , e.getMessage());
            }
        }
        modelMap.put("result" , result);

        return "remove-suggestion";
    }

    @RequestMapping(value = {"/accept-suggestion" , "/accept-suggestion/{EXPERT_ID}/{ORDER_ID}/{SUGGESTION_ID}"}, method = RequestMethod.GET)
    @RolesAllowed({"ADMIN" , "EXPERT"})
    public String acceptSuggestion(final ModelMap modelMap , @PathVariable(value = "EXPERT_ID") final String strExpertId , @PathVariable(value = "ORDER_ID") final String strOrderId , @PathVariable(value = "SUGGESTION_ID") final String strSuggestionId)
    {
        final long expertId = checkStrId(modelMap , strExpertId , "Invalid expert id");
        final long suggestionId = checkStrId(modelMap , strSuggestionId , "Invalid suggestion id");
        final long orderId = checkStrId(modelMap , strOrderId , "Invalid order id");

        boolean result = false;
        if (expertId > 0 && suggestionId > 0 && orderId > 0)
        {
            try
            {
                result = orderService.acceptExpert(expertId , orderId , suggestionId);
            }
            catch (NotFoundUserException | NotFoundOrderException | NotFoundSuggestionException e)
            {
                modelMap.put("error" , e.getMessage());
            }
        }
        modelMap.put("result" , result);

        return "accept-suggestion";
    }

    private long checkStrId(final ModelMap modelMap , final String strId , final String errorMessage)
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

    @RequestMapping(value = {"/order-payment" , "/order-payment/{ORDER_ID}"}, method = RequestMethod.GET)
    @RolesAllowed({"ADMIN"})
    public String acceptSuggestion(final ModelMap modelMap , @PathVariable(value = "ORDER_ID") final String strOrderId)
    {
        final long orderId = checkStrId(modelMap , strOrderId , "Invalid order id");
        if (orderId > 0)
        {
            boolean result = false;
            try
            {
                result = orderService.payment(orderId , 0);// price yani key padakht online anjam shode , agar 0 bashe az hesab customer kasr mishe
            }
            catch (NotFoundOrderException | NotFoundSuggestionException | ThePaymentAmountIsInsufficient | ThisOrderHasBeenPaidException e)
            {
                modelMap.put("error" , e.getMessage());
            }

            modelMap.put("result" , result);
        }

        return "order-payment";
    }

    @RequestMapping(value = "/add-subservice", method = RequestMethod.GET)
    @RolesAllowed({"ADMIN"})
    public String addSubService(final ModelMap modelMap)
    {
        modelMap.put("mainServices" , mainServiceForAddSubServiceMapper.toDtoMainServiceForAddSubServices(mainServicesService.mainServices()));

        return "add-subservice";
    }

    @RequestMapping(value = "/add-subservice", method = RequestMethod.POST)
    @RolesAllowed({"ADMIN"})
    public String addSubService(final ModelMap modelMap , @ModelAttribute("addSubService") final DTOAddSubService dtoAddSubService)
    {
        modelMap.put("mainServices" , mainServiceForAddSubServiceMapper.toDtoMainServiceForAddSubServices(mainServicesService.mainServices()));

        boolean result = false;
        try
        {
            result = subServiceService.addNewSubService(dtoAddSubService);
        }
        catch (InvalidIdException | InvalidPriceException | FoundSubServiceException e)
        {
            modelMap.put("error" , e.getMessage());
        }

        modelMap.put("result" , result);

        return "add-subservice";
    }


    @RequestMapping(value = "/add-main-service", method = RequestMethod.GET)
    @RolesAllowed({"ADMIN"})
    public String addMainService()
    {
        return "add-main-service";
    }

    @RequestMapping(value = "/add-main-service", method = RequestMethod.POST)
    @RolesAllowed({"ADMIN"})
    public String addMainService(final ModelMap modelMap , @RequestParam(value = "name") final String name)
    {
        boolean result = false;
        try
        {
            result = mainServicesService.addCustomMainService(name);
        }
        catch (FoundMainServiceException | NullPointerException e)
        {
            modelMap.put("error" , e.getMessage());
        }

        modelMap.put("result" , result);

        return "add-main-service";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register()
    {
        return "register";
    }

    @RequestMapping(value = "/register-expert", method = RequestMethod.POST)
    public String register(final ModelMap modelMap , @ModelAttribute("addNewUser") final DTOExpertRegister dtoRegister)
    {
        final UserType userType = cheUserTypeRegister(modelMap , dtoRegister.getUserType());

        boolean result = false;
        if (userType != null)
        {
            if (userType.equals(UserType.EXPERT))
            {
                try
                {

                    final MultipartFile image = dtoRegister.getImage();

                    if (image != null)
                    {
                        dtoRegister.setImg(image.getBytes());

                        result = expertService.register(dtoRegister);
                    }
                    else modelMap.put("error" , "Image is null");
                }
                catch (ImageSizeException | NullPointerException | FoundEmailException | InvalidPasswordException | InvalidPostalCodeException | IOException | InvalidImageException e)
                {
                    if (e instanceof IOException) modelMap.put("error" , "Image is invalid!");
                    else modelMap.put("error" , e.getMessage());
                }
            }
            else modelMap.put("error" , "Invalid user type");
        }

        modelMap.put("result" , result);

        return "register";
    }

    @RequestMapping(value = "/register-customer", method = RequestMethod.POST)
    public String register(final ModelMap modelMap , @ModelAttribute("addNewUser") final DTOCustomerRegister dtoRegister)
    {
        final UserType userType = cheUserTypeRegister(modelMap , dtoRegister.getUserType());

        boolean result = false;
        if (userType != null)
        {
            if (userType.equals(UserType.CUSTOMER))
            {
                try
                {
                    result = customerService.register(dtoRegister);
                }
                catch (FoundEmailException | InvalidPasswordException | NullPointerException | InvalidPostalCodeException e)
                {
                    modelMap.put("error" , e.getMessage());
                }
            }
            else modelMap.put("error" , "Invalid user type");
        }

        modelMap.put("result" , result);

        return "register";
    }

    private UserType cheUserTypeRegister(final ModelMap modelMap , final String userTypeStr)
    {
        try
        {
            if (Str.notEmpty(userTypeStr) && !userTypeStr.toUpperCase(Locale.ROOT).equals("ADMIN"))
                return UserType.valueOf(userTypeStr.toUpperCase(Locale.ROOT));
            else throw new Exception();
        }
        catch (Exception e)
        {
            modelMap.put("error" , "Invalid user type");
        }

        return null;
    }

}
