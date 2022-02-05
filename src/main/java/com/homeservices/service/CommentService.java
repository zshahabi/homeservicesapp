package com.homeservices.service;

import com.homeservices.data.entity.Comments;
import com.homeservices.data.entity.Customer;
import com.homeservices.data.entity.Order;
import com.homeservices.data.repository.CommentRepository;
import com.homeservices.exception.NotFoundOrderException;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<Comments> byOrderComment(final long orderId)
    {
        return repository.findByOrderId(orderId);
    }

    public List<Comments> byCustomer(final long customerId)
    {
        return repository.findByCustomerId(customerId);
    }

    public List<Comments> byCommentsOrderCustomer(final long customerId , final long orderId)
    {
        return repository.findByCustomerIdAndOrderId(customerId , orderId);
    }
}
