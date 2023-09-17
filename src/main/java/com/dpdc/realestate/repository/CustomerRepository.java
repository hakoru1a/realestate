package com.dpdc.realestate.repository;

import com.dpdc.realestate.models.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByUsername(String username);
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByPhone(String phone);

    @Modifying
    @Query("UPDATE Customer c SET c.password = :newPassword WHERE c.id = :customerId")
    int updatePasswordById(@Param("customerId") Integer customerId, @Param("newPassword") String newPassword);

    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.isActive = :newActive WHERE c.id = :customerId")
    int updateActiveStatusById(@Param("customerId") Integer customerId, @Param("newActive") boolean newActive);
}
