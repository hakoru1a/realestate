package com.dpdc.realestate.apis;

import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.exception.DeleteDataException;
import com.dpdc.realestate.exception.NotFoundException;
import com.dpdc.realestate.exception.SaveDataException;
import com.dpdc.realestate.models.entity.Blog;
import com.dpdc.realestate.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/blogs/")
public class BlogAPI {

    @Autowired
    private BlogService blogService;

    @Autowired
    private Environment env;

    @GetMapping
    public ResponseEntity<ModelResponse> getBlogs(
            @RequestParam(required = false, defaultValue = "1") String page) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1 ,
                Integer.parseInt(env.getProperty("app.page.size")));
        Page<Blog> blogs = blogService.getBlogs(pageable);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                blogs), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ModelResponse> addBlog(@RequestBody @Valid Blog blog , BindingResult result)
            throws BindException {
            if (result.hasErrors()) {
                throw new BindException(result);
            }
            Blog savedBlog = blogService.addBlog(blog);
            return new
                    ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                    savedBlog), HttpStatus.OK);
    }

    @PatchMapping("/publish/{blogId}/")
    public ResponseEntity<ModelResponse> publishBlog(@PathVariable Integer blogId) throws Exception {
        try{
            blogService.publishBlog(blogId);
            return new
                    ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                    null), HttpStatus.OK);
        }
        catch (NotFoundException e){
            throw e;
        }
        catch (Exception exception){
            throw new Exception(env.getProperty("db.notify.save_fail"));
        }
    }

    @DeleteMapping("/{blogId}")
    public ResponseEntity<ModelResponse> deleteBlog(@PathVariable Integer blogId) throws Exception {
        try{
            blogService.deleteBlogById(blogId);
            return new
                    ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                    null), HttpStatus.NO_CONTENT);
        }
        catch (DeleteDataException | NotFoundException e){
            throw e;
        }
        catch (Exception exception){
            throw new Exception(env.getProperty("db.notify.delete_fail"));
        }
    }

    @PutMapping("/{blogId}/")
    public ResponseEntity<ModelResponse> updateTitleOrContent(@PathVariable Integer blogId, @RequestBody Map<String, String> blog)
            throws Exception {
        try{
            String title = blog.getOrDefault("title", null);
            String content = blog.getOrDefault("content", null);
            Blog savedBlog = blogService.updateContentOrTitle(blogId,title, content);
            return new
                    ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                    savedBlog), HttpStatus.OK);
        }
        catch (NotFoundException | HttpMessageNotReadableException e){
            throw e;
        }
        catch (Exception exception){
            throw new Exception(env.getProperty("db.notify.save_fail"));
        }
    }

}
