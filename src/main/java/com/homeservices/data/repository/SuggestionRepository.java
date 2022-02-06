package com.homeservices.data.repository;

import com.homeservices.data.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long>
{
    Suggestion findByExpertIdAndOrderId(final long expertId , final long orderId);

    List<Suggestion> findByOrderId(final long orderId);

    List<Suggestion> findByOrderIdAndOrderCustomerId(final long orderId , final long customerId);

    Suggestion findByOrderIdAndExpertIdAndOrderCustomerId(final long orderId , final long expertId , final long customerId);
}
