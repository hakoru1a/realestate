package com.dpdc.realestate.apis;

import com.dpdc.realestate.exception.UnauthorizedAllowException;
import com.dpdc.realestate.models.entity.Comment;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.service.CommentService;
import com.dpdc.realestate.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments/")
public class CommentAPI {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private Environment env;

    @PostMapping("/customer/{id}/")
    public Comment addComment(@RequestBody Comment comment, @PathVariable(name = "id") Integer propertyId){
        return null;
    }

    @PostMapping("/user/{id}/")
    public Comment reply(@RequestBody Comment comment, @PathVariable(name = "id") Integer propertyId){
        return null;

    }
}
