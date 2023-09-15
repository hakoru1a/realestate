package com.dpdc.realestate.apis;

import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.dto.request.CredentialRegister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users/")
public class UserAPI {
    @PostMapping("/register/")
    public ResponseEntity<ModelResponse> registerUser(@RequestBody @Valid CredentialRegister credential
            , BindingResult result) throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        return new ResponseEntity<>(new ModelResponse("pass validation", credential), HttpStatus.OK);
    }


}
