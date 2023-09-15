package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.models.entity.User;
import com.dpdc.realestate.repository.UserRepository;
import com.dpdc.realestate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
//    @Autowired
//    private CustomerRepository customerRepository;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findById(1).get();
        if (user != null) {
            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleName()));
            return new org.springframework.security.core.userdetails.User(user.getUsername(),
                    user.getPassword(), authorities);
        }
//        Customer customer = customerRepository.findCustomerByUsername(s);
//        if (customer != null) {
//            Set<GrantedAuthority> authorities = new HashSet<>();
//            return new org.springframework.security.core.userdetails.User(customer.getUsername(),
//                    customer.getPassword(), authorities);
//        }
        throw new UsernameNotFoundException("User does not exist!");
    }
}
