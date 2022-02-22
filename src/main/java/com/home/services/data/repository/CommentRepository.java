package com.home.services.data.repository;

import com.home.services.data.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long>
{
    List<Comments> findByOrderId(final long orderId);

    List<Comments> findByUserEmail(final String customerEmail);

    List<Comments> findByUserId(final long customerId);

    List<Comments> findByUserIdAndOrderId(final long customerId , final long orderId);
}
