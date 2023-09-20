package com.dpdc.realestate.service;

import com.dpdc.realestate.models.entity.Comment;

public interface CommentService {

    Comment addComment(Comment comment, Integer propertyId);

    void deleteCommentById(Integer id);
}
