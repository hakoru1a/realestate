package com.dpdc.realestate.apis;

import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.dto.request.ChangePassword;
import com.dpdc.realestate.dto.request.CredentialRegister;
import com.dpdc.realestate.dto.request.ProfileCustomer;
import com.dpdc.realestate.dto.request.ProfileUser;
import com.dpdc.realestate.exception.DataAlreadyExistException;
import com.dpdc.realestate.exception.PasswordException;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.Role;
import com.dpdc.realestate.models.entity.User;
import com.dpdc.realestate.service.CustomerService;
import com.dpdc.realestate.service.UserService;
import com.dpdc.realestate.service.ValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users/")
public class UserAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ValidationService validationService;
    @PostMapping
    public ResponseEntity<ModelResponse> registerUser(@RequestBody @Valid CredentialRegister credential
            , BindingResult result) throws Exception {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        validationService.validateCredential(credential, false);
        try {
            User newUser = mapper.map(credential, User.class);
            // default role = staff
            User savedUser = userService.register(newUser);
            return new
                    ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                    savedUser), HttpStatus.OK);
        }
        catch (Exception ex){
            throw new Exception();
        }
    }
    @PutMapping("/update-profile/")
    public ResponseEntity<ModelResponse> updateProfile( @RequestBody @Valid ProfileUser profile
            ,BindingResult result) throws Exception{
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        validationService.validateCredential(profile, false);
        User existingUser = userService.findById(profile.getId());
        mapper.map(profile, existingUser);
        User savedUser = userService.register(existingUser);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                savedUser), HttpStatus.OK);
    }
    @PatchMapping("/change-password/{id}/")
    public ResponseEntity<ModelResponse> changePassword(
            @RequestBody @Valid ChangePassword passwords,
            @PathVariable Integer id, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        User existingUser = userService.findById(id);
        if (!passwordEncoder.matches(passwords.getPreviousPassword(), existingUser.getPassword())) {
            throw new PasswordException(env.getProperty("api.notify.no_matches"));
        }
        User savedUser = userService.updatePassword(id, passwordEncoder.encode(passwords.getCurrentPassword()));
        if (savedUser == null) {
            throw new Exception(env.getProperty("api.notify.change_password_fail"));
        }
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), savedUser), HttpStatus.OK);
    }

    @PatchMapping("/change-status/{id}/")
    public ResponseEntity<ModelResponse> changeStatus(@RequestBody Map<String, Boolean> activeMap, @PathVariable Integer id) throws Exception {
        boolean isActive = activeMap.get("active");
        User savedUser = userService.updateStatus(id, isActive);
        if (savedUser == null) {
            throw new Exception(env.getProperty("api.notify.change_status_fail"));
        }
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), savedUser), HttpStatus.OK);
    }
}
