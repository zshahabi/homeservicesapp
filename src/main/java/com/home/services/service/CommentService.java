package com.home.services.service;

import com.home.services.data.entity.Comments;
import com.home.services.data.entity.Order;
import com.home.services.data.entity.User;
import com.home.services.data.enums.Roles;
import com.home.services.data.repository.CommentRepository;
import com.home.services.data.repository.UserRepository;
import com.home.services.exception.NotFoundOrderException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public record CommentService(CommentRepository commentRepository , OrderService orderService)
{

    public boolean addComment(final long orderId , final String email , final String comment) throws NotFoundOrderException
    {
        final Optional<Order> orderFindById = orderService.orderRepository().findById(orderId);
        if (orderFindById.isPresent())
        {
            final Order order = orderFindById.get();

            final UserRepository userRepository = orderService.customerService().userRepository();

            final User userFindByEmail = userRepository.findByEmail(email);

            final Roles role = userFindByEmail.getRoles().get(0);

            if (role.equals(Roles.ADMIN) || role.equals(Roles.EXPERT))
                return addComment(comment , userFindByEmail , order);
            else
            {
                if (order.getCustomer().getEmail().equals(email))
                    return addComment(comment , userFindByEmail , order);
                else throw new AccessDeniedException("You do not have access to this order");
            }
        }
        else throw new NotFoundOrderException(orderId);
    }

    private boolean addComment(final String textComment , final User user , final Order order)
    {
        Comments comments = new Comments();
        comments.setComment(textComment);
        comments.setUser(user);
        comments.setOrder(order);

        comments = commentRepository.save(comments);

        return comments.getId() > 0;
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
