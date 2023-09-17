package com.dpdc.realestate.repository;

import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.id = :userId")
    int updatePasswordById(@Param("userId") Integer userId, @Param("newPassword") String newPassword);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isActive = :newActive WHERE u.id = :userId")
    int updateActiveStatusById(@Param("userId") Integer userId, @Param("newActive") boolean newActive);
}