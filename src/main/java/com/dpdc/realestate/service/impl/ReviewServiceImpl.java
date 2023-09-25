package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.exception.DataAlreadyExistException;
import com.dpdc.realestate.exception.NotFoundException;
import com.dpdc.realestate.handler.EntityCheckHandler;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.Review;
import com.dpdc.realestate.models.entity.User;
import com.dpdc.realestate.repository.ReviewRepository;
import com.dpdc.realestate.repository.UserRepository;
import com.dpdc.realestate.service.CustomerService;
import com.dpdc.realestate.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Set;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private Environment env;

    @Override
    public Page<Review> getReviews(Integer staffId, Pageable pageable) {
        return reviewRepository.findByStaffId(staffId, pageable);
    }

    @Override
    public Review addReview(Integer staffId, Review review) {
        Customer customer = customerService.getCurrentCredential();
        User staff = EntityCheckHandler.checkEntityExistById(userRepository, staffId);
        if (!staff.getRole().getRoleName().equals("ROLE_STAFF")){
            throw new NotFoundException(env.getProperty("db.notify.not_found") + " not a staff");
        }
        boolean isExist = reviewRepository.existsByStaffIdAndCustomerId(staffId, customer.getId());
        if (isExist){
            throw new DataAlreadyExistException(
                   new HashMap<>()
            );
        }
        review.setCustomer(customer);
        review.setStaff(staff);
        return reviewRepository.save(review);
    }

    @Override
    public void deleteReviews(Integer staffId, Set<Integer> reviewIds) {
        EntityCheckHandler.checkEntityExistById(userRepository, staffId);
        reviewRepository.deleteReviews(staffId, reviewIds);
    }


}
