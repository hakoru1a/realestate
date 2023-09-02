package com.dpdc.realestate.apis;


import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.dto.request.Mail;
import com.dpdc.realestate.dto.request.SMS;
import com.dpdc.realestate.exception.MessageSendingException;
import com.dpdc.realestate.service.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/helper/")
public class HelperAPI {
    @Autowired
    private HelperService helperService;
    @PostMapping
    @RequestMapping("/message/")
    public ResponseEntity<ModelResponse> sendMessage(@RequestBody SMS sms){
        boolean isSent = helperService.sendMessage(sms.getToPhone(), sms.getContent());
        if (isSent) {
            ModelResponse modelResponse = new ModelResponse("Message sent successfully", null);
            return new ResponseEntity<>(modelResponse, HttpStatus.OK);
        } else {
            throw new MessageSendingException("Failed to send the message");
        }
    }

    @PostMapping
    @RequestMapping("/mail/")
    public ResponseEntity<ModelResponse> sendMail(@RequestBody Mail mail){
        Map<String, String> model = new HashMap<>();
        model.put("Name", "Chương");
        model.put("location", "Bangalore,India");
        boolean isSent = helperService.sendMail(mail, model);
        if (isSent) {
            ModelResponse modelResponse = new ModelResponse("Message sent successfully", null);
            return new ResponseEntity<>(modelResponse, HttpStatus.OK);
        } else {
            throw new MessageSendingException("Failed to send the message");
        }
    }
}
