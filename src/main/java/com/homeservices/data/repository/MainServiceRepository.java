package com.homeservices.data.repository;

import com.homeservices.data.entity.MainService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainServiceRepository extends JpaRepository<MainService, Long>
{
}
