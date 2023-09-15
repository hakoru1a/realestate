package com.dpdc.realestate.apis;

import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.dto.request.CredentialRegister;
import com.dpdc.realestate.exception.DataAlreadyExistException;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.User;
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
@RequestMapping("/api/users/")
public class UserAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CustomerService customerService;


    @PostMapping
    public ResponseEntity<ModelResponse> registerUser(@RequestBody @Valid CredentialRegister credential
            , BindingResult result) throws Exception {
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
            User newUser = mapper.map(credential, User.class);
            User savedUser = userService.register(newUser);
            return new
                    ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                    savedUser), HttpStatus.OK);
        }
        catch (Exception ex){
            throw new Exception();
        }
    }


}
