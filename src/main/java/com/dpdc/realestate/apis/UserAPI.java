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
import com.dpdc.realestate.service.RoleService;
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
@CrossOrigin
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
    private RoleService roleService;

    @Autowired
    private ValidationService validationService;
    @PostMapping
    public ResponseEntity<ModelResponse> registerUser(@RequestBody @Valid CredentialRegister credential,
            @RequestParam(defaultValue = "1") String roleId
            , BindingResult result) throws Exception {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        validationService.validateCredential(credential, false);
        try {
            User newUser = mapper.map(credential, User.class);
            newUser.setPassword(env.getProperty("app.default_password"));
            Integer ro = Integer.valueOf(roleId);
            newUser.setRole(roleService.getRoleById(ro));
            newUser.setIsActive(true);
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
    @PutMapping("/update-profile/{userId}/{roleId}/admin")
    public ResponseEntity<ModelResponse> updateProfileAdmin( @RequestBody
                                                                 @Valid CredentialRegister profile
            ,@PathVariable Integer userId
            ,@PathVariable Integer roleId
            ,BindingResult result) throws Exception{
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        validationService.validateCredential(userId, profile, false);
        User existingUser = userService.findById(userId);
        existingUser.setUsername(profile.getUsername());
        existingUser.setEmail(profile.getEmail());
        existingUser.setPhone(profile.getPhone());
        existingUser.setFullname(profile.getFullname());
        existingUser.setRole(roleService.getRoleById(roleId));
        existingUser.setPassword(env.getProperty("app.default_password"));
        User savedUser = userService.register(existingUser);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), savedUser), HttpStatus.OK);
    }
    @GetMapping("/get-all/")
    public ResponseEntity<ModelResponse> getAllUser() throws Exception {
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                userService.getAllUser()), HttpStatus.OK);
    }
    @GetMapping("/details/{userId}/")
    public ResponseEntity<ModelResponse> getUsserById(@PathVariable Integer userId)  {
        User user = userService.findById(userId);
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                user), HttpStatus.OK);
    }
}
