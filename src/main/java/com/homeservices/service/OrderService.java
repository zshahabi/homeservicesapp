package com.homeservices.service;

import com.homeservices.data.entity.Address;
import com.homeservices.data.entity.Customer;
import com.homeservices.data.entity.Experts;
import com.homeservices.data.entity.Order;
import com.homeservices.data.entity.OrderStatus;
import com.homeservices.data.entity.SubService;
import com.homeservices.data.repository.OrderRepository;
import com.homeservices.dto.DTOAddOrder;
import com.homeservices.exception.NotFoundSubServiceException;
import com.homeservices.exception.NotFoundUserException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record OrderService(OrderRepository repository , CustomerService customerService , ExpertService expertService ,
                           AddressService addressService , SubServicesService subServicesService)
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

                    Address address = addressService.addAddress(dtoAddOrder.getAddress());

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
            else throw new NotFoundUserException("Not found expert id: " + dtoAddOrder.getExpert());
        }
        else throw new NotFoundUserException("Not found customer id: " + dtoAddOrder.getCustomer());
    }
}