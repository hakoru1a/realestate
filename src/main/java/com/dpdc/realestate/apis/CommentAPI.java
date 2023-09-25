package com.dpdc.realestate.apis;

import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.exception.ForbiddenException;
import com.dpdc.realestate.exception.NotFoundException;
import com.dpdc.realestate.exception.UnauthorizedAllowException;
import com.dpdc.realestate.models.entity.Comment;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.service.CommentService;
import com.dpdc.realestate.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/comments/")
public class CommentAPI {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private Environment env;



    @PostMapping("/{propertyId}/")
    public ResponseEntity<ModelResponse> addComment(@RequestBody @Valid Comment comment,
                                                    @PathVariable(name = "propertyId") Integer propertyId ,
                                                    BindingResult result) throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        Comment addedComment = commentService.addComment(comment, propertyId);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), addedComment),
                HttpStatus.OK);
    }

    @GetMapping("/{propertyId}/")
    public ResponseEntity<ModelResponse> getComments(@PathVariable(name = "propertyId")
                                                    Integer propertyId,
                                                     @RequestParam(defaultValue = "1") String page) throws Exception {
        try{
            Pageable pageable  =
                    PageRequest.of(Integer.parseInt(page) - 1, Integer.parseInt(env.getProperty("app.page.size")));
            Page<Comment> comments = commentService.getComments(propertyId, pageable);
            return new
                    ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), comments),
                    HttpStatus.OK);
        }
        catch (NotFoundException notFoundException){
            throw notFoundException;
        }
        catch (Exception exception){
            throw new Exception(env.getProperty("db.notify.not_found "));
        }
    }

    @DeleteMapping("/{commentId}/")
    public ResponseEntity<ModelResponse> deleteComment(
            @PathVariable(name = "commentId") Integer commentId) throws Exception {
        try{
            commentService.deleteCommentById(commentId);
            return new
                    ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), null),
                    HttpStatus.NO_CONTENT);
        }
        catch (NotFoundException | ForbiddenException e){
            throw e;
        }
        catch (Exception ex){
            throw  new Exception(env.getProperty("db.notify.delete_fail"));
        }
    }

}
