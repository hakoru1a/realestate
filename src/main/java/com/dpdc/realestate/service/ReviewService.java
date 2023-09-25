package com.dpdc.realestate.service;

import com.dpdc.realestate.models.entity.Review;
import io.swagger.models.auth.In;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface ReviewService {

    Page<Review> getReviews(Integer staffId, Pageable pageable);

    Review addReview(Integer staffId, Review review);

    void deleteReviews(Integer staffId, Set<Integer> reviewIds);
}
