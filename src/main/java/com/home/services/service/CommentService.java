package com.home.services.service;

import com.home.services.data.entity.Comments;
import com.home.services.data.entity.Order;
import com.home.services.data.entity.User;
import com.home.services.data.repository.CommentRepository;
import com.home.services.exception.NotFoundOrderException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public record CommentService(CommentRepository commentRepository , OrderService orderService)
{

    public boolean addComment(final long orderId , final String comment) throws NotFoundOrderException
    {
        final Optional<Order> orderFindById = orderService.orderRepository().findById(orderId);
        if (orderFindById.isPresent())
        {
            final Order order = orderFindById.get();

            final User customer = order.getCustomer();

            Comments comments = new Comments();
            comments.setComment(comment);
            comments.setUser(customer);
            comments.setOrder(order);

            comments = commentRepository.save(comments);

            return comments.getId() > 0;

        }
        else throw new NotFoundOrderException(orderId);
    }

    public List<Comments> getCommentsByOrder(final long orderId)
    {
        return commentRepository.findByOrderId(orderId);
    }

    public List<Comments> getCommentsByUser(final String customerEmail)
    {
        return commentRepository.findByUserEmail(customerEmail);
    }

    public List<Comments> getCommentsByUser(final long customerId)
    {
        return commentRepository.findByUserId(customerId);
    }

    public List<Comments> getComments(final long customerId , final long orderId)
    {
        return commentRepository.findByUserIdAndOrderId(customerId , orderId);
    }
}
