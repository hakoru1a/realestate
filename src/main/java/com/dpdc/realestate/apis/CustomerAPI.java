package com.dpdc.realestate.apis;

import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.dto.request.ChangePassword;
import com.dpdc.realestate.dto.request.CredentialRegister;
import com.dpdc.realestate.dto.request.ProfileCustomer;
import com.dpdc.realestate.exception.DataAlreadyExistException;
import com.dpdc.realestate.exception.PasswordException;
import com.dpdc.realestate.models.entity.Customer;
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
import java.util.Map;


@RestController
@RequestMapping("/api/customers/")
public class CustomerAPI {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private Environment env;
    @PostMapping
    public ResponseEntity<ModelResponse> registerUser(@RequestBody @Valid CredentialRegister credential
            , BindingResult result) throws Exception{
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        validationService.validateCredential(credential, false);
        try {
            Customer newCustomer = mapper.map(credential,Customer.class);
            Customer savedCustomer = customerService.register(newCustomer);
            return new
                    ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), savedCustomer), HttpStatus.OK);
        }
        catch (Exception ex){
            throw new Exception();
        }
    }
    @PutMapping("/update-profile/")
    public ResponseEntity<ModelResponse> updateProfile( @RequestBody  @Valid ProfileCustomer profile
    ,BindingResult result) throws Exception{
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        validationService.validateCredential(profile, true);
        Customer existingCustomer = customerService.findById(profile.getId());
        mapper.map(profile, existingCustomer);
        Customer savedCustomer = customerService.register(existingCustomer);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), savedCustomer), HttpStatus.OK);
    }


    @PatchMapping("/{id}/change-password/")
    public ResponseEntity<ModelResponse> changePassword(
            @RequestBody @Valid ChangePassword passwords,
            @PathVariable Integer id, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        Customer existingCustomer = customerService.findById(id);
        if (!passwordEncoder.matches( passwords.getPreviousPassword(), existingCustomer.getPassword())){
            throw new PasswordException(env.getProperty("api.notify.no_matches"));
        }
        Customer savedCustomer = customerService.
                updatePassword(id, passwordEncoder.encode(passwords.getCurrentPassword()));
        if (savedCustomer == null){
            throw  new Exception(env.getProperty("api.notify.change_password_fail"));
        }
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), savedCustomer), HttpStatus.OK);
    }
    @PatchMapping("/{id}/change-status/")
    public ResponseEntity<ModelResponse> changeStatus(@RequestBody Map<String, Boolean> activeMap, @PathVariable Integer id) throws Exception {
        boolean isActive = activeMap.get("active");
        Customer savedCustomer = customerService.updateStatus(id, isActive);
        if (savedCustomer == null){
            throw  new Exception(env.getProperty("api.notify.change_status_fail"));
        }
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), savedCustomer), HttpStatus.OK);
    }

}
