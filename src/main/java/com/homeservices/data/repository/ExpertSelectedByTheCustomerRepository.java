package com.homeservices.data.repository;

import com.homeservices.data.entity.ExpertSelectedByTheCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpertSelectedByTheCustomerRepository extends JpaRepository<ExpertSelectedByTheCustomer, Long>
{
    ExpertSelectedByTheCustomer findByExpertIdAndCustomerId(final long expertId , final long customerId);

    List<ExpertSelectedByTheCustomer> findByCustomerId(final long customerId);
}
