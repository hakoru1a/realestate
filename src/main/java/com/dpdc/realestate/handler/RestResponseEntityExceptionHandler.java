package com.dpdc.realestate.handler;


import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.exception.DataAlreadyExistException;
import com.dpdc.realestate.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Configuration
@ControllerAdvice
@ComponentScan(basePackages = {
        "com.dpdc"
})
public class RestResponseEntityExceptionHandler    {
    private final String error = "Handle request failure";
    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ModelResponse> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(new ModelResponse("lỗi lord",null), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ModelResponse> handleNotFoundException(Exception ex) {
        return new ResponseEntity<>(new ModelResponse(error,null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ModelResponse> customHandleBindException(BindException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<Map<String, String>> errors = new ArrayList<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            Map<String, String> error = new HashMap<>();
            error.put(fieldError.getField().toLowerCase(), fieldError.getDefaultMessage());
            errors.add(error);
        }
        ModelResponse response = new ModelResponse(error, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataAlreadyExistException.class)
    protected ResponseEntity<ModelResponse> handleDataAlreadyExistException(DataAlreadyExistException ex) {
        Map<String, String> errors = ex.getErrors();
        ModelResponse response = new ModelResponse(error, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
