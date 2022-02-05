package com.homeservices.service;

import com.homeservices.config.SpringConfig;
import com.homeservices.data.entity.Comments;
import com.homeservices.exception.NotFoundOrderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest
{
    private CommentService commentService;

    @BeforeEach
    void setUp()
    {
        SpringConfig.config();
        commentService = SpringConfig.newInstance(CommentService.class);
    }

    @Test
    void addComment()
    {
        try
        {
            boolean addComment = commentService.addComment(1 , "THIS IS COMMENT");


            assertTrue(addComment);

        }
        catch (NotFoundOrderException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    void byOrderComment()
    {
        List<Comments> comments = commentService.byOrderComment(2);

        assertNotNull(comments);
    }

    @Test
    void byCustomer()
    {
        List<Comments> comments = commentService.byCustomer(2);

        assertNotNull(comments);
    }

    @Test
    void byCommentsOrderCustomer()
    {
        List<Comments> comments = commentService.byCommentsOrderCustomer(2 , 2);

        assertNotNull(comments);
    }
}