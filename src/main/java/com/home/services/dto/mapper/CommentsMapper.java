package com.home.services.dto.mapper;

import com.home.services.data.entity.Comments;
import com.home.services.dto.DTOComments;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public final class CommentsMapper
{
    public List<DTOComments> toDtoComments(final List<Comments> comments)
    {
        final List<DTOComments> dtoComments = new ArrayList<>();

        if (comments != null)
        {
            for (final Comments comment : comments)
            {
                DTOComments dtoComment = new DTOComments();
                dtoComment.setId(comment.getId());
                dtoComment.setText(comment.getComment());
                dtoComment.setUser(comment.getUser().getEmail());
                dtoComment.setCreatedAt(comment.getCreatedAt().toString());

                dtoComments.add(dtoComment);
            }
        }

        return dtoComments;
    }
}
