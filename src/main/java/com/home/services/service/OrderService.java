package com.home.services.service;

import com.home.services.data.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public record OrderService(OrderRepository orderRepository)
{
}
