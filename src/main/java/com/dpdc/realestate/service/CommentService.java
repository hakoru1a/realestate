package com.dpdc.realestate.service;

import com.dpdc.realestate.models.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    Comment addComment(Comment comment, Integer propertyId);

    void deleteCommentById(Integer id);

    Page<Comment> getComments(Integer propertyId,  Pageable pageable);
}
