package com.dpdc.realestate.apis;

import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.dto.response.BillDTO;
import com.dpdc.realestate.exception.BodyBadRequestException;
import com.dpdc.realestate.exception.NotFoundException;
import com.dpdc.realestate.service.RegisterPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/register-package/")
public class RegisterPackageAPI {

    @Autowired
    private RegisterPackageService registerPackageService;

    @Autowired
    private Environment env;
    @PostMapping("/bill/{customerId}/")
    public ResponseEntity<ModelResponse> getBill(@PathVariable Integer customerId,
                                                 @RequestBody Map<String, Integer> pack
                                                 ) throws Exception {
        Integer quantity = pack.getOrDefault("quantity", 0);
        Integer packageId = pack.getOrDefault("id", 0);
        try{
            BillDTO billDTO =  registerPackageService.checkBill(packageId, customerId ,quantity );
            return new
                    ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), billDTO),
                    HttpStatus.OK);
        }
        catch (HttpMessageNotReadableException | BodyBadRequestException | NotFoundException e){
            throw e;
        }
        catch (Exception exception){
            throw  new Exception(env.getProperty("db.notify.update_fail"));
        }
    }

}
