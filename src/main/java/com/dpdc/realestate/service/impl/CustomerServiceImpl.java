package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.User;
import com.dpdc.realestate.repository.CustomerRepository;
import com.dpdc.realestate.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.persistence.Cacheable;
import java.util.Optional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Customer register(Customer newCustomer)  {
        newCustomer.setPassword(passwordEncoder.encode(newCustomer.getPassword()));
        return customerRepository.save(newCustomer);
    }
    private Customer getCurrentCredential() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return findByUsername(authentication.getName()).orElse(null);
    }
    @Override
    public boolean isExistByUsername(String username, boolean isExcept) {
        if (isExcept) {
            // Trường hợp cập nhật: Bạn có thể xử lý kiểm tra trùng lặp ở đây
            // Đảm bảo rằng bạn không kiểm tra trùng lặp cho chính người dùng đang cập nhật
            // Trả về true nếu username đã tồn tại cho người dùng khác, ngược lại trả về false
            Customer currentCredential = getCurrentCredential();
            Optional<Customer> existingUser = customerRepository.findByUsername(username);
            return existingUser.isPresent()
                    && !existingUser.get().getId().equals(currentCredential.getId()); // Trùng lặp với người dùng khác
        } else {
            // Trường hợp tạo mới: Bạn có thể xử lý kiểm tra trùng lặp ở đây
            // Trả về true nếu username đã tồn tại, ngược lại trả về false
            return customerRepository.findByUsername(username).isPresent();
        }
    }

    @Override
    public boolean isExistByPhone(String phone, boolean isExcept) {
        if (isExcept) {
            Customer currentCredential = getCurrentCredential();
            Optional<Customer> existingUser = customerRepository.findByPhone(phone);
            return existingUser.isPresent()
                    && !existingUser.get().getId().equals(currentCredential.getId()); // Trùng lặp với người dùng khác
        } else {
            return customerRepository.findByPhone(phone).isPresent();
        }
    }

    @Override
    public boolean isExitsByEmail(String email, boolean isExcept) {
        if (isExcept) {
            Customer currentCredential = getCurrentCredential();
            Optional<Customer> existingUser = customerRepository.findByEmail(email);
            return existingUser.isPresent()
                    && !existingUser.get().getId().equals(currentCredential.getId()); // Trùng lặp với người dùng khác
        } else {
            return customerRepository.findByEmail(email).isPresent();
        }
    }

    @Override
    public Customer findById(Integer id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<Customer> findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer updatePassword(Integer id, String newPassword) {
        int updated = customerRepository.updatePasswordById(id, newPassword);
        if (updated > 0){
            return findById(id);
        }
        return null;
    }

    @Override
    public Customer updateStatus(Integer id, Boolean isActive) {
        int updated = customerRepository.updateActiveStatusById(id, isActive);
        if (updated > 0){
            return findById(id);
        }
        return null;
    }


}
