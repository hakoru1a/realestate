package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.handler.EntityCheckHandler;
import com.dpdc.realestate.models.entity.Comment;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.Property;
import com.dpdc.realestate.repository.CommentRepository;
import com.dpdc.realestate.repository.PropertyRepository;
import com.dpdc.realestate.service.CommentService;
import com.dpdc.realestate.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;


    @Autowired
    private PropertyRepository propertyRepository;

    @Override
    public Comment addComment(Comment comment, Integer propertyId) {
        Property property = EntityCheckHandler.checkEntityExistById(propertyRepository, propertyId);
        comment.setProperty(property);
        return commentRepository.save(comment);
    }

    @Override
    public void deleteCommentById(Integer id) {

    }
}
