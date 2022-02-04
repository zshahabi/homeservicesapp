package com.homeservices.service;

import com.homeservices.data.entity.Address;
import com.homeservices.data.entity.Customer;
import com.homeservices.data.entity.Experts;
import com.homeservices.data.entity.Order;
import com.homeservices.data.enums.OrderStatus;
import com.homeservices.data.entity.SubService;
import com.homeservices.data.entity.Suggestion;
import com.homeservices.data.repository.OrderRepository;
import com.homeservices.dto.DTOAddOrder;
import com.homeservices.exception.NotFoundExpertException;
import com.homeservices.exception.NotFoundOrderException;
import com.homeservices.exception.NotFoundSubServiceException;
import com.homeservices.exception.NotFoundSuggestionException;
import com.homeservices.exception.NotFoundUserException;
import com.homeservices.exception.ThePaymentAmountIsInsufficient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public record OrderService(OrderRepository repository , CustomerService customerService , ExpertService expertService ,
                           SubServicesService subServicesService ,
                           SuggestionService suggestionService)
{

    public boolean addOrder(final DTOAddOrder dtoAddOrder) throws NotFoundUserException, NotFoundSubServiceException
    {
        Customer customer = customerService.getUserById(dtoAddOrder.getCustomer());
        if (customer != null)
        {
            Optional<Experts> byId = expertService.repository().findById(dtoAddOrder.getExpert());

            if (byId.isPresent())
            {
                SubService subService = subServicesService.byName(dtoAddOrder.getName());

                if (subService != null)
                {
                    Experts expert = byId.get();

                    Address address = customerService.addressService().addAddress(dtoAddOrder.getAddress());

                    Order order = new Order();
                    order.setAddress(address);
                    order.setCustomer(customer);
                    order.setExperts(expert);
                    order.setSubService(subService);
                    order.setName(dtoAddOrder.getName());
                    order.setDescription(dtoAddOrder.getDescription());
                    order.setOrderStatus(OrderStatus.waiting_for_specialist_selection);

                    order = repository().save(order);

                    return order.getId() > 0;

                }
                else throw new NotFoundSubServiceException(dtoAddOrder.getName());
            }
            else throw new NotFoundUserException("expert" , dtoAddOrder.getExpert());
        }
        else throw new NotFoundUserException("customer" , dtoAddOrder.getCustomer());
    }

    public boolean changeStatus(final long orderId , final OrderStatus orderStatus) throws NotFoundOrderException
    {
        Optional<Order> byOrderId = repository.findById(orderId);
        if (byOrderId.isPresent())
        {
            Order order = byOrderId.get();

            order.setOrderStatus(orderStatus);

            repository.save(order);

            return true;
        }
        else throw new NotFoundOrderException(orderId);
    }

    public boolean acceptExpert(final long expertId , final long orderId) throws NotFoundUserException, NotFoundOrderException
    {
        final ResultCheckExpertOrderId expertOrderId = checkExpertOrderId(orderId , expertId);
        if (expertOrderId != null)
        {
            Suggestion suggestion = suggestionService().repository().findByExpertIdAndOrderId(expertId , orderId);

            if (suggestion != null)
            {
                Order order = expertOrderId.order();
                order.setExperts(expertOrderId.expert());
                repository.save(order);

                return true;
            }
        }

        return false;
    }

    public boolean removeExpert(final long expertId , final long orderId) throws NotFoundOrderException, NotFoundUserException, NotFoundExpertException
    {
        ResultCheckExpertOrderId expertOrderId = checkExpertOrderId(orderId , expertId);
        if (expertOrderId != null)
        {
            final Order byIdAndExpertsId = repository.findByIdAndExpertsId(orderId , expertId);
            if (byIdAndExpertsId != null)
            {
                Order order = expertOrderId.order();

                order.setExperts(null);

                repository.save(order);

            }
            else throw new NotFoundExpertException();
        }

        return false;
    }

    private ResultCheckExpertOrderId checkExpertOrderId(final long orderId , final long expertId) throws NotFoundUserException, NotFoundOrderException
    {
        Optional<Order> byOrderId = repository.findById(orderId);
        if (byOrderId.isPresent())
        {
            Optional<Experts> byExpertId = expertService.repository().findById(expertId);
            if (byExpertId.isPresent()) return new ResultCheckExpertOrderId(byOrderId.get() , byExpertId.get());
            else throw new NotFoundUserException("expert" , expertId);
        }
        else throw new NotFoundOrderException(orderId);
    }

    public boolean payment(final long orderId , final int price) throws NotFoundOrderException, NotFoundSuggestionException, ThePaymentAmountIsInsufficient
    {
        Optional<Order> byOrderId = repository.findById(orderId);
        if (byOrderId.isPresent())
        {
            List<Suggestion> suggestion = suggestionService.repository().findByOrderId(orderId);

            if (suggestion != null && suggestion.size() > 0)
            {
                Order order = byOrderId.get();

                int orderPrice = order.getSubService().getPrice();

                final int pricePayment = (price == 0) ? order.getCustomer().getAccountCredit() : price;

                if (pricePayment < orderPrice)
                {
                    if (price == 0)
                    {
                        Customer customer = order.getCustomer();
                        customer.setAccountCredit(pricePayment - orderPrice);
                        customerService.repository().save(customer);
                    }

                    Experts expert = order.getExperts();

                    expert.setAccountCredit(expert.getAccountCredit() + pricePayment);
                    expertService.repository().save(expert);

                    return changeStatus(orderId , OrderStatus.paid);
                }
                else throw new ThePaymentAmountIsInsufficient(orderPrice , pricePayment);
            }
            else throw new NotFoundSuggestionException();
        }
        else throw new NotFoundOrderException(orderId);
    }

    public record ResultCheckExpertOrderId(Order order , Experts expert)
    {
    }
}
