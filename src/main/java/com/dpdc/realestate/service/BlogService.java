package com.dpdc.realestate.service;

import com.dpdc.realestate.models.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

public interface BlogService {

    Blog addBlog(Blog blog);

    Page<Blog> getBlogs(Pageable pageable);

    void deleteBlogById(Integer blogId);

    void publishBlog(Integer blogId);

    Blog updateContentOrTitle(Integer blogId,String title, String content);
}
