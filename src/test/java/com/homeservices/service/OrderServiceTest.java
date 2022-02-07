package com.homeservices.service;

import com.homeservices.config.SpringConfig;
import com.homeservices.data.enums.OrderStatus;
import com.homeservices.dto.DTOAddOrder;
import com.homeservices.dto.DTOAddress;
import com.homeservices.exception.NotFoundExpertException;
import com.homeservices.exception.NotFoundOrderException;
import com.homeservices.exception.NotFoundSubServiceException;
import com.homeservices.exception.NotFoundSuggestionException;
import com.homeservices.exception.NotFoundUserException;
import com.homeservices.exception.ThePaymentAmountIsInsufficient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest
{

    private OrderService orderService;

    @BeforeEach
    void setUp()
    {
        SpringConfig.config();
        orderService = SpringConfig.newInstance(OrderService.class);
    }

    @Test
    void addOrder()
    {
        DTOAddOrder dtoAddOrder = new DTOAddOrder();
        dtoAddOrder.setCustomer(1);
        dtoAddOrder.setDescription("DESCRIPTION");
        dtoAddOrder.setName("NAME_ORDER");
        dtoAddOrder.setSubServiceName("SUB_SERVICE");
        dtoAddOrder.setExpert(2);

        DTOAddress dtoAddress = new DTOAddress();
        dtoAddress.setAlley("ADD ORDER ALLEY");
        dtoAddress.setStreet("ADD ORDER STREET");
        dtoAddress.setPostalCode(1234566789);

        dtoAddOrder.setAddress(dtoAddress);

        try
        {
            boolean addOrder = orderService.addOrder(dtoAddOrder);

            assertTrue(addOrder);
        }
        catch (NotFoundUserException | NotFoundSubServiceException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void changeStatus()
    {
        try
        {
            boolean changeStatus = orderService.changeStatus(2 , OrderStatus.started);

            assertTrue(changeStatus);
        }
        catch (NotFoundOrderException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void acceptExpert()
    {
        try
        {
            boolean acceptExpert = orderService.acceptExpert(2 , 1);

            assertTrue(acceptExpert);
        }
        catch (NotFoundUserException | NotFoundOrderException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void removeExpert()
    {
        try
        {
            boolean removeExpert = orderService.removeExpert(2 , 1);

            assertTrue(removeExpert);
        }
        catch (NotFoundOrderException | NotFoundUserException | NotFoundExpertException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void payment()
    {
        try
        {
            boolean payment = orderService.payment(2 , 0);

            assertTrue(payment);
        }
        catch (NotFoundOrderException | NotFoundSuggestionException | ThePaymentAmountIsInsufficient e)
        {
            e.printStackTrace();
        }
    }
}