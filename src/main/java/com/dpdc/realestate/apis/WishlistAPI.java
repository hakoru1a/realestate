package com.dpdc.realestate.apis;


import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.models.entity.Property;
import com.dpdc.realestate.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/wishlist/")
public class WishlistAPI {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private Environment env;
    @GetMapping("/{customerId}/")
    public ResponseEntity<ModelResponse> getWishlist(@PathVariable Integer customerId
            , @RequestParam(required = false, defaultValue = "1") String page){
        Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1 , Integer.parseInt(env.getProperty("app.page.size")));
        Page<Property> wishlist =  wishlistService.getWishlist(customerId, pageable);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), wishlist),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/{propertyId}/{customerId}", method = {RequestMethod.POST, RequestMethod.DELETE})
    public ResponseEntity<ModelResponse> addOrRemoveWishlist(@PathVariable Integer customerId,
                                                           @PathVariable Integer propertyId){
        Property property = wishlistService.addOrRemoveWishlist(propertyId, customerId);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), property),
                HttpStatus.OK);
    }

}
