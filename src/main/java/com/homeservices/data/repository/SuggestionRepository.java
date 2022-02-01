package com.homeservices.data.repository;

import com.homeservices.data.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long>
{
    Suggestion findByExpertIdAndOrderId(final long expertId , final long orderId);

    Suggestion findByOrderId(final long orderId);
}
