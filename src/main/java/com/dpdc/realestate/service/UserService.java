package com.dpdc.realestate.service;

import com.dpdc.realestate.models.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User register(User newUser) ;

    boolean isExistByUsername(String username) ;

    boolean isExistByPhone(String phone);

    boolean isExitsByEmail(String email);
}
