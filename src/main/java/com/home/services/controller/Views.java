package com.home.services.controller;

import com.home.services.data.entity.Order;
import com.home.services.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@Controller
@RequestMapping(value = "/", method = RequestMethod.GET)
public record Views(OrderService orderService)
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
}
