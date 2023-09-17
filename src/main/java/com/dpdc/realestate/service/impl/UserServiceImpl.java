package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.User;
import com.dpdc.realestate.repository.CustomerRepository;
import com.dpdc.realestate.repository.UserRepository;
import com.dpdc.realestate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s).orElse(null);
        if (user != null) {
            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleName()));
            return new org.springframework.security.core.userdetails.User(user.getUsername(),
                    user.getPassword(), authorities);
        }
        Customer customer = customerRepository.findByUsername(s).orElse(null);
        if (customer != null) {
            Set<GrantedAuthority> authorities = new HashSet<>();
            return new org.springframework.security.core.userdetails.User(customer.getUsername(),
                    customer.getPassword(), authorities);
        }
        throw new UsernameNotFoundException("User does not exist!");
    }

    @Override
    public User register(User newUser) {
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            return userRepository.save(newUser);
    }


    @Override
    public boolean isExistByUsername(String username, boolean isExcept) {
        if (isExcept) {
            // Trường hợp cập nhật: Bạn có thể xử lý kiểm tra trùng lặp ở đây
            // Trả về true nếu username đã tồn tại cho người dùng khác, ngược lại trả về false
            User currentUser = getCurrentUser();
            Optional<User> existingUser = userRepository.findByUsername(username);
            return existingUser.isPresent()
                    && !existingUser.get().getId().equals(currentUser.getId()); // Trùng lặp với người dùng khác
        } else {
            // Trường hợp tạo mới: Bạn có thể xử lý kiểm tra trùng lặp ở đây
            // Trả về true nếu username đã tồn tại, ngược lại trả về false
            return userRepository.findByUsername(username).isPresent();
        }
    }
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName()).orElse(null);
    }
    @Override
    public boolean isExistByPhone(String phone, boolean isExcept) {
        if (isExcept) {
            // Trường hợp cập nhật: Bạn có thể xử lý kiểm tra trùng lặp ở đây
            // Trả về true nếu số điện thoại đã tồn tại cho người dùng khác, ngược lại trả về false
            User currentUser = getCurrentUser();
            Optional<User> existingUser = userRepository.findByPhone(phone);
            return existingUser.isPresent()
                    && !existingUser.get().getId().equals(currentUser.getId()); // Trùng lặp với người dùng khác
        } else {
            // Trường hợp tạo mới: Bạn có thể xử lý kiểm tra trùng lặp ở đây
            // Trả về true nếu số điện thoại đã tồn tại, ngược lại trả về false
            return userRepository.findByPhone(phone).isPresent();
        }
    }

    @Override
    public boolean isExitsByEmail(String email, boolean isExcept) {
        if (isExcept) {
            // Trường hợp cập nhật: Bạn có thể xử lý kiểm tra trùng lặp ở đây
            // Trả về true nếu email đã tồn tại cho người dùng khác, ngược lại trả về false
            User currentUser = getCurrentUser();
            Optional<User> existingUser = userRepository.findByEmail(email);
            return existingUser.isPresent()
                    && !existingUser.get().getId().equals(currentUser.getId()); // Trùng lặp với người dùng khác
        } else {
            // Trường hợp tạo mới: Bạn có thể xử lý kiểm tra trùng lặp ở đây
            // Trả về true nếu email đã tồn tại, ngược lại trả về false
            return userRepository.findByEmail(email).isPresent();
        }
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updatePassword(Integer id, String newPassword) {
        int updated = userRepository.updatePasswordById(id, newPassword);
        if (updated > 0) {
            return findById(id);
        }
        return null;
    }
    @Override
    public User updateStatus(Integer id, Boolean isActive) {
        int updated = userRepository.updateActiveStatusById(id, isActive);
        if (updated > 0) {
            return findById(id);
        }
        return null;
    }

}
