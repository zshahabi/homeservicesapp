package com.homeservices.data.repository;

import com.homeservices.data.entity.SubService;
import com.homeservices.service.SubServicesService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubServiceRepository extends JpaRepository<SubService, Long>
{
    SubService findByName(final String name);
}
