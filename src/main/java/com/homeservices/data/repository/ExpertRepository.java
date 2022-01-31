package com.homeservices.data.repository;

import com.homeservices.data.entity.Experts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertRepository extends JpaRepository<Experts, Long>
{
}
