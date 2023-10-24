package com.dpdc.realestate.apis;

import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.dto.request.ChangePassword;
import com.dpdc.realestate.dto.request.CredentialRegister;
import com.dpdc.realestate.dto.request.Mail;
import com.dpdc.realestate.dto.request.ProfileCustomer;
import com.dpdc.realestate.exception.DataAlreadyExistException;
import com.dpdc.realestate.exception.PasswordException;
import com.dpdc.realestate.exception.RejectException;
import com.dpdc.realestate.jwt.JwtTokenUtils;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.service.CustomerService;
import com.dpdc.realestate.service.HelperService;
import com.dpdc.realestate.service.UserService;
import com.dpdc.realestate.service.ValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Path;
import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/customers/")
@CrossOrigin
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
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private HelperService helperService;

    @Autowired
    private Environment env;

    @GetMapping("/{token}/")
    public ResponseEntity<ModelResponse> activeCustomer(@PathVariable String token){
        String username = jwtTokenUtils.getUsernameFromToken(token);
        customerService.activeByUsername(username);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                null), HttpStatus.OK);
    }
    @GetMapping("/active/{username}/")
    public ResponseEntity<ModelResponse> activeCustomerForAdmin(@PathVariable String username){
        customerService.activeByUsername(username);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                username), HttpStatus.OK);
    }
    @DeleteMapping("/{customerId}/")
    public ResponseEntity<ModelResponse> deleteCustomerId(@PathVariable Integer customerId){
        customerService.deleteById(customerId);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                null), HttpStatus.OK);
    }

    @GetMapping("/get-all/")
    public ResponseEntity<ModelResponse> getAllCustomer(){
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                customerService.getAllCustomer()), HttpStatus.OK);
    }
    @GetMapping("/details/{customerId}/")
    public ResponseEntity<ModelResponse> getCustomerById(@PathVariable Integer customerId){
        Customer customer = customerService.findById(customerId);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                customer), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<ModelResponse> registerUser(@RequestBody @Valid CredentialRegister credential
            , BindingResult result) throws Exception{
        if (result.hasErrors()) {
            throw new BindException(result);
        }
            validationService.validateCredential(credential, false);
            Customer newCustomer = mapper.map(credential,Customer.class);
            newCustomer.setPassword(env.getProperty("app.default_password"));
            Customer savedCustomer = customerService.register(newCustomer);
            savedCustomer.setTimes(1);
            savedCustomer.setIsActive(false);
            UserDetails userDetails = userService.findCustomerByUsername(savedCustomer.getUsername(), true);
            // Send mail to active
            String token = jwtTokenUtils.generateToken(userDetails, true);
            Mail mail = new Mail("Active your account", "Kich hoạt tại khoản",
                    "2051052012chuong@ou.edu.vn");
            Map<String, String> map = new HashMap<>();
            map.put("token", token);
            helperService.sendMail(mail,map, env.getProperty("app.active_account"));
            return new
                    ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                    savedCustomer), HttpStatus.OK);

    }
    @PutMapping("/update-profile/")
    public ResponseEntity<ModelResponse> updateProfile( @RequestBody  @Valid ProfileCustomer profile
    ,BindingResult result) throws Exception{
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        validationService.validateCredential(profile, true);
        Customer existingCustomer = customerService.findById(profile.getId());
        String password = existingCustomer.getPassword();
        mapper.map(profile, existingCustomer);
        existingCustomer.setPassword(password);
        Customer savedCustomer = customerService.update(existingCustomer);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), savedCustomer), HttpStatus.OK);
    }

    @PutMapping("/update-profile/{customerId}/admin/")
    public ResponseEntity<ModelResponse> updateProfileAdmin( @RequestBody  @Valid CredentialRegister profile
            ,@PathVariable Integer customerId
            ,BindingResult result) throws Exception{
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        validationService.validateCredential(customerId, profile, true);
        Customer existingCustomer = customerService.findById(customerId);
        existingCustomer.setUsername(profile.getUsername());
        existingCustomer.setEmail(profile.getEmail());
        existingCustomer.setPhone(profile.getPhone());
        existingCustomer.setFullname(profile.getFullname());
        existingCustomer.setPassword(env.getProperty("app.default_password"));
        Customer savedCustomer = customerService.register(existingCustomer);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), savedCustomer), HttpStatus.OK);
    }

    @PatchMapping("/change-password/{id}/")
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
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), savedCustomer), HttpStatus.OK);
    }
    @PatchMapping("/change-status/{id}/")
    public ResponseEntity<ModelResponse> changeStatus(@RequestBody Map<String, Boolean>
                                                                  activeMap,
                                                      @PathVariable Integer id) throws Exception {
        boolean isActive = activeMap.get("active");
        Customer savedCustomer = customerService.updateStatus(id, isActive);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                savedCustomer), HttpStatus.OK);
    }

    @PatchMapping("/avatar/{id}/")
    public ResponseEntity<ModelResponse> updateAvatar(
            @ModelAttribute("image") MultipartFile image,
            @PathVariable Integer id
    ){
        Customer exitsCustomer = customerService.findById(id);
        String url = helperService.uploadImage(image);
        exitsCustomer.setAvatar(url);
        customerService.save(exitsCustomer);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                exitsCustomer), HttpStatus.OK);
    }



}
