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
import com.home.services.exception.ThePaymentAmountIsInsufficient;
import com.home.services.exception.ThisOrderHasBeenPaidException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public record OrderService(OrderRepository orderRepository , SuggestionService suggestionService ,
                           ExpertService expertService , CustomerService customerService ,
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
                order.setPrice(subService.getPrice());
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

    // final int price => yani key padakht online anjam shode , agar 0 bashe az hesab customer kasr mishe
    public boolean payment(final long orderId , final int price) throws NotFoundOrderException, NotFoundSuggestionException, ThePaymentAmountIsInsufficient, ThisOrderHasBeenPaidException
    {
        final Optional<Order> orderFindById = orderRepository.findById(orderId);
        if (orderFindById.isPresent())
        {
            final List<Suggestion> suggestion = suggestionService.suggestionRepository().findByOrderId(orderId);

            if (suggestion != null && suggestion.size() > 0)
            {
                final Order order = orderFindById.get();
                if (!order.getOrderStatus().equals(OrderStatus.PAID))
                {
                    final int orderPrice = order.getSubService().getPrice();

                    final int pricePayment = (price == 0) ? order.getCustomer().getAccountCredit() : price;

                    if (pricePayment <= orderPrice)
                    {
                        if (price == 0)
                        {
                            final User customer = order.getCustomer();
                            customer.setAccountCredit(pricePayment - orderPrice);
                            customerService.userRepository().save(customer);
                        }

                        final User expert = order.getExpert();

                        expert.setAccountCredit(expert.getAccountCredit() + pricePayment);
                        expertService.expertRepository().save(expert);

                        return changeStatus(orderId , OrderStatus.PAID);
                    }
                    else throw new ThePaymentAmountIsInsufficient(orderPrice , pricePayment);
                }
                else throw new ThisOrderHasBeenPaidException(order.getName());
            }
            else throw new NotFoundSuggestionException();
        }
        else throw new NotFoundOrderException(orderId);
    }

    public boolean changeStatus(final long orderId , final OrderStatus orderStatus) throws NotFoundOrderException
    {
        Optional<Order> byOrderId = orderRepository.findById(orderId);
        if (byOrderId.isPresent())
        {
            Order order = byOrderId.get();

            order.setOrderStatus(orderStatus);

            orderRepository.save(order);

            return true;
        }
        else throw new NotFoundOrderException(orderId);
    }

    public record ResultCheckExpertOrderId(Order order , User expert)
    {
    }
}
