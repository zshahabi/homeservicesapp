package com.home.services.data.repository;

import com.home.services.data.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>
{
    List<Order> findAll();

    List<Order> findByCustomerEmail(final String customerEmail);
}
