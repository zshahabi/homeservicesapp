package com.homeservices.data.repository;

import com.homeservices.data.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long>
{
    List<Comments> findByOrderId(final long orderId);

    List<Comments> findByCustomerId(final long customerId);

    List<Comments> findByCustomerIdAndOrderId(final long customerId , final long orderId);
}
