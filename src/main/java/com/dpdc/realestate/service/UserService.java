package com.dpdc.realestate.service;

import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    User register(User newUser) ;
    Optional<User> findByUsername(String username);

    List<User> getAllUser();
    boolean isExistByUsername(String username, boolean isExcept) ;

    boolean isExistByPhone(String phone, boolean isExcept);

    boolean isExitsByEmail(String email, boolean isExcept);

    boolean isExistByUsername(Integer userId,String username, boolean isExcept) ;

    boolean isExistByPhone(Integer userId,String phone, boolean isExcept);

    boolean isExitsByEmail(Integer userId,String email, boolean isExcept);

    User save(User user);

    User findById(Integer id);

    User updatePassword(Integer id, String newPassword);

    User updateStatus(Integer id, Boolean isActive);

    UserDetails findCustomerByUsername(String username,  boolean isRegister);


    User getCurrentCredential();
}
