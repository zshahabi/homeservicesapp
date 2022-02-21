package com.home.services.service;

import com.home.services.data.entity.Order;
import com.home.services.data.entity.SubService;
import com.home.services.data.entity.Suggestion;
import com.home.services.data.entity.User;
import com.home.services.data.enums.OrderStatus;
import com.home.services.data.repository.OrderRepository;
import com.home.services.dto.DTOAddOrder;
import com.home.services.dto.mapper.AddressMapper;
import com.home.services.exception.InvalidPostalCodeException;
import com.home.services.exception.NotFoundOrderException;
import com.home.services.exception.NotFoundSubServiceException;
import com.home.services.exception.NotFoundSuggestionException;
import com.home.services.exception.NotFoundUserException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record OrderService(OrderRepository orderRepository , SuggestionService suggestionService ,
                           ExpertService expertService ,
                           CustomerService customerService ,
                           SubServiceService subServiceService , AddressMapper addressMapper)
{
    public boolean addOrder(final DTOAddOrder dtoAddOrder) throws NotFoundUserException, NotFoundSubServiceException, InvalidPostalCodeException
    {
        final User customer = customerService.userRepository().findByEmail(dtoAddOrder.getEmailCustomer());
        if (customer != null)
        {
            final SubService subService = subServiceService.getSubServiceByName(dtoAddOrder.getSubServiceName());

            if (subService != null)
            {
                Order order = new Order();
                order.setAddress(addressMapper.toAddress(dtoAddOrder));
                order.setCustomer(customer);
                order.setSubService(subService);
                order.setName(dtoAddOrder.getName());
                order.setDescription(dtoAddOrder.getDescription());
                order.setOrderStatus(OrderStatus.WAITING_FOR_SPECIALIST_SELECTION);

                order = orderRepository.save(order);

                return order.getId() > 0;
            }
            else throw new NotFoundSubServiceException(dtoAddOrder.getName());
        }
        else throw new NotFoundUserException("customer" , dtoAddOrder.getEmailCustomer());
    }

    public boolean acceptExpert(final long expertId , final long orderId , final long suggestionId) throws NotFoundUserException, NotFoundOrderException, NotFoundSuggestionException
    {
        final ResultCheckExpertOrderId expertOrderId = checkExpertOrderId(orderId , expertId);
        final Suggestion suggestion = suggestionService().suggestionRepository().findByExpertIdAndId(expertId , suggestionId);

        if (suggestion != null)
        {
            Order order = expertOrderId.order();
            order.setExpert(expertOrderId.expert());
            orderRepository.save(order);

            return true;
        }
        else throw new NotFoundSuggestionException(suggestionId);
    }

    private ResultCheckExpertOrderId checkExpertOrderId(final long orderId , final long expertId) throws NotFoundUserException, NotFoundOrderException
    {
        Optional<Order> orderFindById = orderRepository.findById(orderId);
        if (orderFindById.isPresent())
        {
            User expertFindById = expertService().expertRepository().findById(expertId);
            if (expertFindById != null) return new ResultCheckExpertOrderId(orderFindById.get() , expertFindById);
            else throw new NotFoundUserException("expert" , expertId);
        }
        else throw new NotFoundOrderException(orderId);
    }

    public record ResultCheckExpertOrderId(Order order , User expert)
    {
    }
}
