package com.home.services.data.repository;

import com.home.services.data.entity.MainService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainServiceRepository extends JpaRepository<MainService, Long>
{
    MainService findByName(final String name);

    MainService findById(final long mainServiceId);
}
