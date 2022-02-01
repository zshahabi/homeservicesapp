package com.homeservices.service;

import com.homeservices.data.entity.Comments;
import com.homeservices.data.entity.Customer;
import com.homeservices.data.entity.Order;
import com.homeservices.data.repository.CommentRepository;
import com.homeservices.exception.NotFoundOrderException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record CommentService(CommentRepository repository , OrderService orderService)
{

    public boolean addComment(final long orderId , final String comment) throws NotFoundOrderException
    {
        Optional<Order> byOrderId = orderService.repository().findById(orderId);
        if (byOrderId.isPresent())
        {
            Order order = byOrderId.get();

            Customer customer = order.getCustomer();

            Comments comments = new Comments();
            comments.setComment(comment);
            comments.setCustomer(customer);
            comments.setOrder(order);

            comments = repository.save(comments);

            return comments.getId() > 0;

        }
        else throw new NotFoundOrderException(orderId);
    }
}
