package com.dpdc.realestate.handler;


import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
public class RestResponseEntityExceptionHandler {

    @Autowired
    private Environment env;
    private final String error = "Handle request failure";

    @ExceptionHandler(value = {Exception.class, SaveDataException.class})
    public ResponseEntity<ModelResponse> handleException(Exception ex) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {UnauthorizedAllowException.class})
    public ResponseEntity<ModelResponse> handleUnauthorizedAllowException(UnauthorizedAllowException ex) {
        return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {ForbiddenException.class})
    public ResponseEntity<ModelResponse> handleForbiddenException(ForbiddenException ex) {
        return buildErrorResponse(ex, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ModelResponse> handleNotFoundException(NotFoundException ex) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {BodyBadRequestException.class})
    public ResponseEntity<ModelResponse> handleBodyBadRequestException(BodyBadRequestException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = {RejectException.class})
    public ResponseEntity<ModelResponse> handleRejectException(RejectException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<ModelResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

//    Xử lý binding spring validation
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

    // Data already
    @ExceptionHandler(DataAlreadyExistException.class)
    protected ResponseEntity<ModelResponse> handleDataAlreadyExistException(DataAlreadyExistException ex) {
        Map<String, String> errors = ex.getErrors();
        ModelResponse response = new ModelResponse(env.getProperty("api.notify.already_exist"), errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    private ResponseEntity<ModelResponse> buildErrorResponse(Exception ex, HttpStatus status) {
        String errorMessage = ex.getMessage();
        ModelResponse response = new ModelResponse(errorMessage, null);
        return new ResponseEntity<>(response, status);
    }


    //    Xử lý handle lỗi 1 field
    @ExceptionHandler(ImageSizeException.class)
    protected ResponseEntity<ModelResponse> handleImageSizeException(ImageSizeException ex) {
        return handleException(ex, "image");
    }

    @ExceptionHandler(PasswordException.class)
    protected ResponseEntity<ModelResponse> handlePasswordException(PasswordException ex) {
        return handleException(ex, "password");
    }

    protected ResponseEntity<ModelResponse> handleException(Exception ex, String fieldName) {
        Map<String, String> errors = new HashMap<>();
        errors.put(fieldName, ex.getMessage()); // Set the error message as the message from the custom exception
        ModelResponse response = new ModelResponse(error, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
