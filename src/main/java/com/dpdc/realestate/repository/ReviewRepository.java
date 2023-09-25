package com.dpdc.realestate.repository;

import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Page<Review> findByStaffId(Integer staffId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Review r WHERE r.staff.id = :staffId AND r.id IN :reviewIds")
    void deleteReviews(@Param("staffId") Integer staffId, @Param("reviewIds") Set<Integer> reviewIds);

    boolean existsByStaffIdAndCustomerId(Integer staffId, Integer customerId);


}
