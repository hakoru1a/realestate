package com.dpdc.realestate.apis;


import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.exception.BodyBadRequestException;
import com.dpdc.realestate.exception.NotFoundException;
import com.dpdc.realestate.models.entity.Appointment;
import com.dpdc.realestate.models.entity.Payment;
import com.dpdc.realestate.models.entity.PaymentData;
import com.dpdc.realestate.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/payment/")
public class PaymentAPI {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private Environment env;

    @GetMapping("/{customerId}/")
    public ResponseEntity<ModelResponse> getPayment( @PathVariable Integer customerId){
        Set<PaymentData> payments = paymentService.getPayments(customerId);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), payments),
                HttpStatus.OK);
    }


    @PostMapping("/buy-turn/")
    public ResponseEntity<ModelResponse>  createPaymentBuyTurn(@RequestBody  Map<String, Integer> payment) throws Exception {
        Integer packageId = payment.getOrDefault("packageId", 0);
        Integer customerId = payment.getOrDefault("customerId", 0);
        Integer quantity = payment.getOrDefault("quantity", 0);
        try{
            Payment savedPayment = paymentService.createPaymentFromBuyTurn(packageId, customerId, quantity);
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

    @PostMapping("/booking/")
    public ResponseEntity<ModelResponse> createAppointment(@RequestBody @Valid Appointment appointment,
                                                           BindingResult result) throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        Payment saveAppointment = paymentService.createPaymentFromBookAppointment(appointment);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), saveAppointment),
                HttpStatus.OK);
    }



//    Instant now = Instant.now();
//    ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
//    ZonedDateTime zonedDateTime = now.atZone(zoneId);
//
//    // Định dạng ZonedDateTime thành chuỗi dễ đọc
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//    String formattedTime = zonedDateTime.format(formatter);
//        System.out.println("Thời gian hiện tại: " + formattedTime);


}
