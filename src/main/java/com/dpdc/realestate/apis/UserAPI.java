package com.dpdc.realestate.apis;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/")
public class UserAPI {
    @GetMapping
    public String getCurrentUser(){

        return "abc";
    }
}
