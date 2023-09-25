package com.dpdc.realestate.repository;

import com.dpdc.realestate.models.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE Blog b SET b.isPublish = true, b.publishDate = :currentDate WHERE b.id = :blogId")
    void publishBlog(Integer blogId, Instant currentDate);


}
