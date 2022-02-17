package com.home.services.data.repository;

import com.home.services.data.entity.Expert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertRepository extends JpaRepository<Expert, Long>
{
    Expert findByEmail(final String email);
}
