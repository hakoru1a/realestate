package com.dpdc.realestate.apis;


import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.exception.DeleteDataException;
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
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

    @PostMapping
    public ResponseEntity<ModelResponse> addProperty(@RequestBody @Valid Property property
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

    @PutMapping("/{id}/")
    public ResponseEntity<ModelResponse> updateProperty(@RequestBody @Valid Property property,
                                                        @PathVariable Integer id
            , BindingResult result) throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        Property existProperty = propertyService.findById(id);
        if (existProperty == null) {
            throw new NotFoundException(env.getProperty("db.notify.not_found"));
        }
        Location existingLocation = existProperty.getLocation();
        Location updatedLocation = property.getLocation();
        existingLocation.setCity(updatedLocation.getCity());
        existingLocation.setDistrict(updatedLocation.getDistrict());
        existingLocation.setStreet(updatedLocation.getStreet());
        mapper.map(property, existProperty);
        try {
            Property saveProperty = propertyService.addProperty(property);
            return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                    saveProperty), HttpStatus.OK);
        } catch (Exception exception) {
            throw new SaveDataException(env.getProperty("db.notify.update_fail"));
        }
    }


    @DeleteMapping("/{id}/")
    public ResponseEntity<ModelResponse> deleteProperty(@PathVariable Integer id) {
        try {
            propertyService.softDeletePropertyById(id);
            return new ResponseEntity<>(
                    new ModelResponse(env.getProperty("api.notify.success"), null)
                    , HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            throw new DeleteDataException(env.getProperty("db.notify.delete_fail"));
        }
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<ModelResponse> hardDeleteProperty(@PathVariable Integer id) {
        try {
            propertyService.hardDeletePropertyById(id);
            return new ResponseEntity<>(
                    new ModelResponse(env.getProperty("api.notify.success"), null)
                    , HttpStatus.NO_CONTENT);
        } catch (Exception ex) {
            throw new DeleteDataException(env.getProperty("db.notify.delete_fail"));
        }
    }


}
