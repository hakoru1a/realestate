package com.dpdc.realestate;


import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.exception.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler    {

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ModelResponse> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(new ModelResponse("lỗi lord",null), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<Object> customHandleBindException(BindException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String error = fieldError.getDefaultMessage();
            errors.add(error);
        }
        ModelResponse response = new ModelResponse("Dữ liệu không hợp lệ", errors);
        return  new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
