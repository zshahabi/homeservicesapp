package com.homeservices.data.repository;

import com.homeservices.data.entity.DescriptionCustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptionCustomerOrderRepository extends JpaRepository<DescriptionCustomerOrder, Long>
{
}
