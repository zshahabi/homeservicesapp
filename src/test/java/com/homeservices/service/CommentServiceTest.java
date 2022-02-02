package com.homeservices.service;

import com.homeservices.config.SpringConfig;
import com.homeservices.exception.NotFoundOrderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest
{

    @BeforeEach
    void setUp()
    {
        SpringConfig.config();
    }

    @Test
    void addComment()
    {
        try
        {
            boolean addComment = SpringConfig.newInstance(CommentService.class).addComment(1 , "THIS IS COMMENT");


            assertTrue(addComment);

        }
        catch (NotFoundOrderException e)
        {
            e.printStackTrace();
        }
    }
}