package com.home.services.data.repository;

import com.home.services.data.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>
{
    Customer findByEmail(final String email);
}
