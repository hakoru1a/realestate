package com.dpdc.realestate.apis;


import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.exception.DeleteDataException;
import com.dpdc.realestate.exception.ForbiddenException;
import com.dpdc.realestate.exception.NotFoundException;
import com.dpdc.realestate.exception.SaveDataException;
import com.dpdc.realestate.models.entity.Location;
import com.dpdc.realestate.models.entity.Property;
import com.dpdc.realestate.service.PropertyService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/properties/")
public class PropertyAPI {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private Environment env;

    @Autowired
    private ModelMapper mapper;


    @GetMapping
    public ResponseEntity<ModelResponse> getProperties(@RequestParam Map<String, String> params ) {
        String propertyName = params.get("propertyName");
        Integer categoryId = params.containsKey("categoryId") ? Integer.parseInt(params.get("categoryId")) : null;
        BigDecimal priceFrom = params.containsKey("priceFrom") ? new BigDecimal(params.get("priceFrom")) : null;
        BigDecimal priceTo = params.containsKey("priceTo") ? new BigDecimal(params.get("priceTo")) : null;
        String city = params.get("city");
        String street = params.get("street");
        String district = params.get("district");
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) - 1: 0;
        PageRequest pageable = PageRequest.of(page, Integer.parseInt(env.getProperty("app.page.size")));
        Page<Property> properties = propertyService.getProperties(propertyName, categoryId, priceFrom, priceTo, city,
                street, district, pageable);
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                properties), HttpStatus.OK);
    }

    @GetMapping("/my-property/{customerId}/")
    public ResponseEntity<ModelResponse> getMyProperty(@PathVariable Integer customerId,
                                                       @RequestParam(defaultValue = "1") String page){
        PageRequest pageable = PageRequest.of(Integer.parseInt(page) - 1,
                Integer.parseInt(env.getProperty("app.page.size")));
        Page<Property> properties = propertyService.findMyProperties(customerId, pageable);
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                properties), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ModelResponse> registerProperty(@RequestBody @Valid Property property
            , BindingResult result) throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        try {
            Property saveProperty = propertyService.addProperty(property);
            return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                    saveProperty), HttpStatus.OK);
        } catch (Exception exception) {
            throw new SaveDataException(env.getProperty("db.notify.save_fail"));
        }
    }


    @PostMapping("/assign/{propertyId}/")
    public ResponseEntity<ModelResponse> assignProperty(
            @RequestBody Map<String, Set<Integer>> staffIds,
            @PathVariable Integer propertyId
    ) {
        try {
            Set<Integer> ids = staffIds.getOrDefault("staffIds", new HashSet<>());
            propertyService.assignStaffToProperty(ids, propertyId);
            return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                    null), HttpStatus.OK);
        }
        catch (NotFoundException notFoundException){
            throw  notFoundException;
        }
        catch (Exception exception) {
            throw new SaveDataException(env.getProperty("db.notify.save_fail"));
        }
    }

    @DeleteMapping("/{propertyId}/")
    public ResponseEntity<ModelResponse> deleteProperty(@PathVariable Integer propertyId) throws Exception {
        try{
            propertyService.softDeletePropertyById(propertyId, true);
            return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                    null), HttpStatus.NO_CONTENT);
        }
        catch (ForbiddenException | NotFoundException e){
            throw e;
        }
        catch (Exception ex){
            throw new Exception(env.getProperty("db.notify.delete_fail"));
        }
    }


    @DeleteMapping("/hard/{propertyId}/")
    public ResponseEntity<ModelResponse> hardDeleteProperty(@PathVariable Integer propertyId) {
        try {
            propertyService.hardDeletePropertyById(propertyId);
            return new ResponseEntity<>(
                    new ModelResponse(env.getProperty("api.notify.success"), null)
                    , HttpStatus.NO_CONTENT);
        }
        catch (ForbiddenException | NotFoundException e){
            throw e;
        }
            catch (Exception ex) {
            throw new DeleteDataException(env.getProperty("db.notify.delete_fail"));
        }
    }

    @PutMapping("/{propertyId}/")
    public ResponseEntity<ModelResponse> updateProperty(@RequestBody @Valid Property property,
                                                        @PathVariable Integer propertyId
            , BindingResult result) throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        Property existProperty = propertyService.findById(propertyId);
        Location existingLocation = existProperty.getLocation();
        Location updatedLocation = property.getLocation();
        existingLocation.setCity(updatedLocation.getCity());
        existingLocation.setDistrict(updatedLocation.getDistrict());
        existingLocation.setStreet(updatedLocation.getStreet());
        mapper.map(property, existProperty);
        existProperty.setLocation(existingLocation);
        try {
            Property saveProperty = propertyService.updateProperty(existProperty);
            return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                    saveProperty), HttpStatus.OK);
        } catch (Exception exception) {
            throw new SaveDataException(env.getProperty("db.notify.update_fail"));
        }
    }

    // Check giờ có thể undeleted
    @PatchMapping("/undeleted/{propertyId}/")
    public ResponseEntity<ModelResponse> undeleteProperty(@PathVariable Integer propertyId) throws Exception {
        try{
            propertyService.softDeletePropertyById(propertyId, false);
            return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                    null), HttpStatus.OK);
        }
        catch (ForbiddenException | NotFoundException e){
            throw e;
        }
        catch (Exception ex){
            throw new Exception(env.getProperty("db.notify.delete_fail"));
        }
    }


    @PatchMapping("/active/{propertyId}/")
    public ResponseEntity<ModelResponse> activeProperty(@PathVariable Integer propertyId
            , @RequestBody Map<String,Boolean> mapIsActive ) throws Exception {
        Boolean isActive = mapIsActive.getOrDefault("isActive", false);
        try{
            Property property = propertyService.updatePropertyActiveStatus(propertyId, isActive);
            return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                    property), HttpStatus.OK);
        }
        catch (NotFoundException notFoundException){
            throw notFoundException;
        }
        catch (Exception ex){
            throw new Exception(env.getProperty("api.notify.change_status_fail"));
        }
    }

}
