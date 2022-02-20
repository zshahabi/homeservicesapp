package com.home.services.data.repository;

import com.home.services.data.entity.SubService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubServiceRepository extends JpaRepository<SubService, Long>
{

    @Query("select sub.name from SubService sub")
    List<String> getSubServiceByNames();

    SubService findByName(final String name);
}
