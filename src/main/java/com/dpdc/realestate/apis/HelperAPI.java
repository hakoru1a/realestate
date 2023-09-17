package com.dpdc.realestate.apis;


import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.dto.request.Mail;
import com.dpdc.realestate.dto.request.SMS;
import com.dpdc.realestate.dto.response.TestValidator;
import com.dpdc.realestate.exception.ImageSizeException;
import com.dpdc.realestate.exception.MessageSendingException;
import com.dpdc.realestate.service.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/helper")
public class HelperAPI {
    @Autowired
    private HelperService helperService;
    @Autowired
    private Environment env;
    @PostMapping
    @RequestMapping("/message/")
    @Async
    public void sendMessage(@RequestBody SMS sms){
        helperService.sendMessage(sms.getToPhone(), sms.getContent());
    }
    @PostMapping
    @RequestMapping("/mail/")
    @Async
    public void sendMail(@RequestBody Mail mail){
        Map<String, String> model = new HashMap<>();
        model.put("Name", "Chương");
        model.put("location", "Bangalore,India");
        helperService.sendMail(mail, model);
    }
    @PostMapping
    @RequestMapping("/upload-image/") // Test postman có jpg
    public ResponseEntity<ModelResponse> uploadImage(@ModelAttribute("image") MultipartFile image){
        long maxSizeBytes =  (1024 * 1024)/2; // 10MB in bytes
        if (image.getSize() > maxSizeBytes){
            throw new ImageSizeException(env.getProperty("api.notify.over_size"));
        }
        String imgUrl = helperService.uploadImage(image);
        ModelResponse model = new ModelResponse("upload thành công", imgUrl);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ModelResponse> testValidator (@RequestBody @Valid TestValidator model
            , BindingResult result) throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        return new ResponseEntity<>(new ModelResponse("pass validation", model), HttpStatus.OK);
    }
}
