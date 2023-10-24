package com.dpdc.realestate.apis;


import com.dpdc.realestate.configs.VNPayConfig;
import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.dto.response.BillDTO;
import com.dpdc.realestate.exception.BodyBadRequestException;
import com.dpdc.realestate.exception.NotFoundException;
import com.dpdc.realestate.exception.RejectException;
import com.dpdc.realestate.models.entity.Appointment;
import com.dpdc.realestate.models.entity.Payment;
import com.dpdc.realestate.models.entity.PaymentData;
import com.dpdc.realestate.service.PaymentService;
import com.dpdc.realestate.service.PropertyService;
import com.dpdc.realestate.service.RegisterPackageService;
import com.dpdc.realestate.utils.Utils;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api/payment/")
public class PaymentAPI {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RegisterPackageService registerPackageService;


    @Autowired
    private PropertyService propertyService;

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
    public ResponseEntity<ModelResponse> createPaymentBuyTurn(@RequestBody Map<String, Integer> payment) throws Exception {
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

    @GetMapping("/buy-turn-2/")
    public ResponseEntity<ModelResponse> createPaymentBuyTurn2(@RequestParam Map<String,String> payment) throws Exception {
       return  null;
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


    @Autowired
    private VNPayConfig Config;
        @GetMapping("/create-url-turn/{customerId}/")
    public ResponseEntity<ModelResponse> getUrl(HttpServletRequest req, @RequestParam Map<String, String> pack,
                         @PathVariable Integer customerId) throws UnsupportedEncodingException {
        Integer quantity = Integer.valueOf(pack.getOrDefault("quantity", String.valueOf(1)));
        Integer packageId = Integer.valueOf(pack.getOrDefault("packageId", String.valueOf(0)));
        BillDTO bill = registerPackageService.checkBill(packageId, customerId, quantity);
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = bill.getPack().getPrice().multiply(BigDecimal.valueOf((bill.getQuantity()))).longValue() * 100L;
        String bankCode = req.getParameter("bankCode");
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(req);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl+"pricing/");
        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Payment By Turn:" + vnp_TxnRef +
                "Purchase-" + bill.getQuantity() + "-" + bill.getCustomer().getId() + "-" +
                bill.getPack().getId());
        return getModelResponseResponseEntity(req, orderType, vnp_IpAddr, vnp_Params);
    }



    @GetMapping("/create-url-booking/{customerId}/")
    public ResponseEntity<ModelResponse> getUrlBooking(HttpServletRequest req, @RequestParam Map<String, String> pack,
                                                @PathVariable Integer customerId) throws UnsupportedEncodingException {
        Long epochSeconds = Long.parseLong(pack.getOrDefault("appointmentDate", String.valueOf(0L)));
        Integer propertyId = Integer.valueOf(pack.getOrDefault("propertyId", String.valueOf(0)));
        if (epochSeconds == 0L)
            throw new RejectException("epochSeconds not valid");
        Utils.isValidDateNextWeek(Instant.ofEpochSecond(epochSeconds));
        Appointment appointment = new Appointment(
                propertyId,customerId, Instant.ofEpochSecond(epochSeconds)
        );
        paymentService.isValidAppointment(appointment);
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long amount = 1000000L * 100;
        String bankCode = req.getParameter("bankCode");
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(req);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl+"property/"+propertyId);

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Payment Booking:" + vnp_TxnRef +
                "Purchase-" + epochSeconds + "-" + customerId + "-" + propertyId);
        return getModelResponseResponseEntity(req, orderType, vnp_IpAddr, vnp_Params);
    }

    private ResponseEntity<ModelResponse> getModelResponseResponseEntity(HttpServletRequest req, String orderType, String vnp_IpAddr, Map<String, String> vnp_Params) throws UnsupportedEncodingException {
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = req.getParameter("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String url =  VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), url),
                HttpStatus.OK);
    }


}
