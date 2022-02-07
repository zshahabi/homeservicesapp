package com.homeservices.service;

import com.homeservices.data.entity.DescriptionCustomerOrder;
import com.homeservices.data.repository.DescriptionCustomerOrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record DescriptionCustomerOrderService(DescriptionCustomerOrderRepository repository)
{

    public List<DescriptionCustomerOrder> getAll()
    {
        return repository.findAll();
    }
}
