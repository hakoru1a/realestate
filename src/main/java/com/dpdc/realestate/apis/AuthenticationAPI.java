package com.dpdc.realestate.apis;

import com.dpdc.realestate.dto.JwtResponse;
import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.dto.request.Credential;
import com.dpdc.realestate.exception.RejectException;
import com.dpdc.realestate.jwt.JwtTokenUtils;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.User;
import com.dpdc.realestate.service.CustomerService;
import com.dpdc.realestate.service.UserService;
import com.dpdc.realestate.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping("/authenticate/")
public class AuthenticationAPI {
    @Autowired
    private Utils utils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ModelResponse> createAuthenticationToken(
            @RequestBody @Valid Credential authenticationRequest
    )  {
        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            final UserDetails userDetails = userService
                    .loadUserByUsername(authenticationRequest.getUsername());
            return generateToken(userDetails, false);
        }
        catch (RejectException e){
            throw e;
        }
        catch (Exception exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ModelResponse(exception.getMessage(), null)
            );
        }
    }
    @PostMapping("/refresh-token/")
    public ResponseEntity<ModelResponse> createAccessTokenFromRefreshToken(
            @RequestBody @Valid Map<String, String> requestRefreshToken
    ) {
        try {
            String refreshToken = requestRefreshToken.getOrDefault("refreshToken", null);
            ResponseEntity<ModelResponse> invalidRefreshTokenResponse = ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ModelResponse("Invalid refresh token", null));

            if (refreshToken == null) {
                return invalidRefreshTokenResponse;
            }

            String username = jwtTokenUtils.getUsernameFromToken(refreshToken);
            final UserDetails userDetails = userService.loadUserByUsername(username);

            if (userDetails != null) {
                // Create new token
                return generateToken(userDetails, true);
            } else {
                // Reuse the ResponseEntity for invalid refresh token
                return invalidRefreshTokenResponse;
            }
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ModelResponse("Get auth token failed", null)
            );
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch (DisabledException e) {
            throw new DisabledException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Wrong username or password", e);
        }
    }
    private ResponseEntity<ModelResponse> generateToken(UserDetails userDetails,
                                                        boolean isHandleRefreshToken) {
        final String token = jwtTokenUtils.generateToken(userDetails, false);
        final  String refreshToken = jwtTokenUtils.generateToken(userDetails, true);
        final Date expirationDateToken = this.jwtTokenUtils.getExpirationDateFromToken(token);
        String exToken = this.utils.getDateFormatter().format(expirationDateToken);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ModelResponse("Get auth token successfully", new JwtResponse(token,
                        isHandleRefreshToken ? null : refreshToken ,exToken))
        );
    }

    @GetMapping("/current-customer/")
    public ResponseEntity<ModelResponse> getCurrentCustomer(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = customerService.findByUsername(authentication.getName()).orElse(null);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ModelResponse("Get user successfully", customer)
        );
    }

    @GetMapping("/current-staff/")
    public ResponseEntity<ModelResponse> getCurrentStaff(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName()).get();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ModelResponse("Get user successfully", user)
        );
    }
}
