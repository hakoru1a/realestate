package com.dpdc.realestate.repository;

import com.dpdc.realestate.models.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
