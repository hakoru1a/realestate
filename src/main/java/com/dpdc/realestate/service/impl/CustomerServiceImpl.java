package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.dto.request.CredentialRegister;
import com.dpdc.realestate.exception.NotFoundException;
import com.dpdc.realestate.exception.RejectException;
import com.dpdc.realestate.handler.EntityCheckHandler;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.User;
import com.dpdc.realestate.repository.CustomerRepository;
import com.dpdc.realestate.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.persistence.Cacheable;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Environment env;

    @Override
    public Customer register(Customer newCustomer)  {
        newCustomer.setPassword(passwordEncoder.encode(newCustomer.getPassword()));
        return customerRepository.save(newCustomer);
    }

    @Override
    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    @Override
    public Customer update(Customer update) {
        return customerRepository.save(update);
    }

    @Override
    public Customer getCurrentCredential() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return findByUsername(authentication.getName()).orElse(null);
    }

    @Override
    public void activeByUsername(String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(env.getProperty("db.notify.not_found")));
        customer.setIsActive(true);
        customerRepository.save(customer);
    }

    @Override
    public Customer setTurn(Integer customerId, Integer turn, boolean isMinus) {
        Customer customer = EntityCheckHandler.checkEntityExistById(customerRepository, customerId);
        int myTurn = isMinus ? customer.getTimes() - turn : customer.getTimes() + turn;
        if (myTurn < 0) {
            throw  new RejectException("Turn not valid");
        }
        customer.setTimes(myTurn);
        return customerRepository.save(customer);
    }

    @Override
    public void deleteById(Integer id) {
        customerRepository.deleteById(id);
    }


    @Override
    public boolean isExistByUsername(String username, boolean isExcept) {
        if (isExcept) {
            // Trường hợp cập nhật: Bạn có thể xử lý kiểm tra trùng lặp ở đây
            // Đảm bảo rằng bạn không kiểm tra trùng lặp cho chính người dùng đang cập nhật
            // Trả về true nếu username đã tồn tại cho người dùng khác, ngược lại trả về false
            Customer currentCredential = getCurrentCredential();
            Optional<Customer> existingUser = customerRepository.findByUsername(username);
            if (currentCredential == null){
                return existingUser.isPresent();
            }
            boolean a = !existingUser.get().getId().equals(currentCredential.getId());
            boolean b = existingUser.isPresent();
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
            if (currentCredential == null){
                return existingUser.isPresent();
            }
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
            if (currentCredential == null){
                return existingUser.isPresent();
            }
            return existingUser.isPresent()
                    && !existingUser.get().getId().equals(currentCredential.getId()); // Trùng lặp với người dùng khác
        } else {
            return customerRepository.findByUsername(email).isPresent();
        }
    }

    @Override
    public boolean isExistByUsername(Integer customerId, String username, boolean isExcept) {
        if (isExcept) {
            // Trường hợp cập nhật: Bạn có thể xử lý kiểm tra trùng lặp ở đây
            // Đảm bảo rằng bạn không kiểm tra trùng lặp cho chính người dùng đang cập nhật
            // Trả về true nếu username đã tồn tại cho người dùng khác, ngược lại trả về false
            Customer currentCredential = findById(customerId);
            Optional<Customer> existingUser = customerRepository.findByUsername(username);
            if (currentCredential == null){
                return existingUser.isPresent();
            }
            return existingUser.isPresent()
                    && !existingUser.get().getId().equals(currentCredential.getId()); // Trùng lặp với người dùng khác
        } else {
            // Trường hợp tạo mới: Bạn có thể xử lý kiểm tra trùng lặp ở đây
            // Trả về true nếu username đã tồn tại, ngược lại trả về false
            return customerRepository.findByUsername(username).isPresent();
        }
    }

    @Override
    public boolean isExistByPhone(Integer customerId, String phone, boolean isExcept) {
        if (isExcept) {
            Customer currentCredential = findById(customerId);
            Optional<Customer> existingUser = customerRepository.findByPhone(phone);
            if (currentCredential == null){
                return existingUser.isPresent();
            }
            return existingUser.isPresent()
                    && !existingUser.get().getId().equals(currentCredential.getId()); // Trùng lặp với người dùng khác
        } else {
            return customerRepository.findByPhone(phone).isPresent();
        }
    }

    @Override
    public boolean isExitsByEmail(Integer customerId, String email, boolean isExcept) {
        if (isExcept) {
            Customer currentCredential = findById(customerId);
            Optional<Customer> existingUser = customerRepository.findByEmail(email);
            if (currentCredential == null){
                return existingUser.isPresent();
            }
            return existingUser.isPresent()
                    && !existingUser.get().getId().equals(currentCredential.getId()); // Trùng lặp với người dùng khác
        } else {
            return customerRepository.findByUsername(email).isPresent();
        }
    }



    @Override
    public Customer findById(Integer id) {
        return customerRepository.findById(id).orElseThrow(() ->
                new NotFoundException(env.getProperty("db.notify.not_found")));
    }

    @Override
    public Optional<Customer> findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    // Check sau
    @Override
    public Customer updatePassword(Integer id, String newPassword) {
        EntityCheckHandler.checkEntityExistById(customerRepository, id);
        int updated = customerRepository.updatePasswordById(id, newPassword);
        return findById(id);
    }

    @Override
    public Customer updateStatus(Integer id, Boolean isActive) {
        EntityCheckHandler.checkEntityExistById(customerRepository, id);
        int updated = customerRepository.updateActiveStatusById(id, isActive);
        return findById(id);
    }


}
