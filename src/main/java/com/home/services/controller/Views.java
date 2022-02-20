package com.home.services.controller;

import com.home.services.data.entity.Order;
import com.home.services.dto.DTOAddOrder;
import com.home.services.exception.InvalidPostalCodeException;
import com.home.services.exception.NotFoundSubServiceException;
import com.home.services.exception.NotFoundUserException;
import com.home.services.service.OrderService;
import com.home.services.service.SubServiceService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Controller
@RequestMapping(value = "/")
public record Views(OrderService orderService , SubServiceService subServiceService)
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
    @RolesAllowed("ADMIN")
    public String addNewOrderView(final ModelMap model)
    {
        model.put("subServiceNames" , getSubServiceNames());
        return "add-new-order";
    }

    @RequestMapping(value = "/add-new-order", method = RequestMethod.POST)
    @RolesAllowed("ADMIN")
    public String addNewOrder(final ModelMap model , @ModelAttribute("dtoAddNewOrder") DTOAddOrder dtoAddOrder , Authentication authentication)
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
}
