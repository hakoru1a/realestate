package com.dpdc.realestate.apis;


import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.exception.BodyBadRequestException;
import com.dpdc.realestate.exception.NotFoundException;
import com.dpdc.realestate.models.entity.Payment;
import com.dpdc.realestate.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment/")
public class PaymentAPI {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private Environment env;

    @GetMapping("/{customerId}/")
    public ResponseEntity<ModelResponse> getPayment(@RequestParam(defaultValue = "1")
                                                        String page, @PathVariable Integer customerId){
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1 , Integer.parseInt(env.getProperty("app.page.size")));
        Page<Payment> payments = paymentService.getPayments(customerId,pageable);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), payments),
                HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<ModelResponse>  createPayment(@RequestBody  Map<String, Integer> payment) throws Exception {
        Integer packageId = payment.getOrDefault("packageId", 0);
        Integer customerId = payment.getOrDefault("customerId", 0);
        Integer quantity = payment.getOrDefault("quantity", 0);
        try{
            Payment savedPayment = paymentService.createPayment(packageId, customerId, quantity);
            return new
                    ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), savedPayment),
                    HttpStatus.OK);
        }
        catch (HttpMessageNotReadableException | NotFoundException | BodyBadRequestException e){
            throw  e;
        }
        catch (Exception exception){
            throw new Exception(env.getProperty("db.notify.save_fail"));
        }
    }





}
