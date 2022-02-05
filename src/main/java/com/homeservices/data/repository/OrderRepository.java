package com.homeservices.data.repository;

import com.homeservices.data.entity.Order;
import com.homeservices.data.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>
{
    Order findByIdAndExpertsId(final long orderId , final long expertId);

    List<Order> findByOrderStatus(final OrderStatus orderStatus);
}
