package com.dpdc.realestate.service;

import com.dpdc.realestate.models.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User register(User newUser) ;

    boolean isExistByUsername(String username, boolean isExcept) ;

    boolean isExistByPhone(String phone, boolean isExcept);

    boolean isExitsByEmail(String email, boolean isExcept);

    User save(User user);

    User findById(Integer id);

    User updatePassword(Integer id, String newPassword);

    User updateStatus(Integer id, Boolean isActive);
}
