package com.dpdc.realestate.apis;

import com.dpdc.realestate.models.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/")
public class UserApi {
    @GetMapping
    public User get(){
        User u = new User();
        u.setPassword("Admin@123");
        u.setUsername("devcangio");
        return u;
    }
}
