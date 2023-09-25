package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.exception.ForbiddenException;
import com.dpdc.realestate.handler.EntityCheckHandler;
import com.dpdc.realestate.models.entity.Comment;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.Property;
import com.dpdc.realestate.repository.CommentRepository;
import com.dpdc.realestate.repository.CustomerRepository;
import com.dpdc.realestate.repository.PropertyRepository;
import com.dpdc.realestate.service.CommentService;
import com.dpdc.realestate.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;


    @Override
    public Comment addComment(Comment comment, Integer propertyId) {
        Property property = EntityCheckHandler.checkEntityExistById(propertyRepository, propertyId);
        EntityCheckHandler.checkEntityExistById(customerRepository, comment.getCustomer().getId());
        comment.setProperty(property);
        return commentRepository.save(comment);
    }

    @Override
    public void deleteCommentById(Integer id) {
        isMyComment(id);
        commentRepository.deleteById(id);
    }

    @Override
    public Page<Comment> getComments(Integer propertyId, Pageable pageable) {
        Property property = EntityCheckHandler.checkEntityExistById(propertyRepository, propertyId);
        return commentRepository.findByProperty(property, pageable);
    }

    private void isMyComment(Integer id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Comment comment = EntityCheckHandler.checkEntityExistById(commentRepository, id);
        if (authentication.getAuthorities().stream().anyMatch(auth ->
                "ROLE_CUSTOMER".equals(auth.getAuthority()))){
            Customer customer = customerService.getCurrentCredential();
            if (commentRepository.findByCustomerAndId(customer, comment.getId()).isEmpty()) {
                throw new ForbiddenException("Access denied: Comment does not belong to the current customer");
            }
        }
    }
}
