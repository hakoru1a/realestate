package com.dpdc.realestate.repository;

import com.dpdc.realestate.models.entity.Comment;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Optional<Comment> findByCustomerAndId(Customer customer, Integer id);

    Page<Comment> findByProperty(Property property,
                                 Pageable pageable);
}
