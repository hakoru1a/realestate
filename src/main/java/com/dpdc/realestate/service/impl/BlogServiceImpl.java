package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.handler.EntityCheckHandler;
import com.dpdc.realestate.models.entity.Blog;
import com.dpdc.realestate.models.entity.User;
import com.dpdc.realestate.repository.BlogRepository;
import com.dpdc.realestate.repository.UserRepository;
import com.dpdc.realestate.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public Blog addBlog(Blog blog) {
        User user = EntityCheckHandler.checkEntityExistById(userRepository, blog.getUser().getId());
        blog.setUser(user);
        return blogRepository.save(blog);
    }

    @Override
    public Page<Blog> getBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public void deleteBlogById(Integer blogId) {
        EntityCheckHandler.checkEntityExistById(blogRepository, blogId);
        blogRepository.deleteById(blogId);
    }

    @Override
    public void publishBlog(Integer blogId) {
        EntityCheckHandler.checkEntityExistById(blogRepository, blogId);
        blogRepository.publishBlog(blogId, Instant.now());
    }

    @Override
    public Blog updateContentOrTitle(Integer blogId ,String title, String content) {
        Blog blog=  EntityCheckHandler.checkEntityExistById(blogRepository, blogId);
        blog.setTitle(title);
        blog.setContent(content);
        return blogRepository.save(blog);
    }
}
