package com.dpdc.realestate.apis;

import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.dto.request.CredentialRegister;
import com.dpdc.realestate.dto.request.ProfileCustomer;
import com.dpdc.realestate.exception.DataAlreadyExistException;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.service.CustomerService;
import com.dpdc.realestate.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/customers/")
public class CustomerAPI {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;
    @PostMapping
    public ResponseEntity<ModelResponse> registerUser(@RequestBody @Valid CredentialRegister credential
            , BindingResult result) throws Exception{
        // Check lỗi input
        if (result.hasErrors()) {
            throw new BindException(result);
        }

        Map<String, String> errors = new HashMap<>();
        if (customerService.isExistByPhone(credential.getPhoneNumber()) ||
                userService.isExistByPhone(credential.getPhoneNumber())){
            errors.put("phone", env.getProperty("api.notify.already_exist"));
        }
        if (customerService.isExistByUsername(credential.getUsername()) ||
                userService.isExistByUsername(credential.getUsername())){
            errors.put("username", env.getProperty("api.notify.already_exist"));
        }
        if (customerService.isExitsByEmail(credential.getEmail()) ||
                userService.isExitsByEmail(credential.getEmail())){
            errors.put("email", env.getProperty("api.notify.already_exist"));
        }
        if (!errors.isEmpty())
            throw new DataAlreadyExistException(errors);

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
    @PostMapping("/{id}/update-profile/")
    public ResponseEntity<ModelResponse> updateProfile(@PathVariable Integer id,
                                                       @RequestBody  ProfileCustomer profile
    ,BindingResult result) throws Exception{
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        return null;
    }
}
