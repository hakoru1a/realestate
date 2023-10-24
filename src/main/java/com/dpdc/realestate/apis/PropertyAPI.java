package com.dpdc.realestate.apis;


import com.cloudinary.Cloudinary;
import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.dto.request.AddProperty;
import com.dpdc.realestate.exception.*;
import com.dpdc.realestate.models.entity.Aminitie;
import com.dpdc.realestate.models.entity.Media;
import com.dpdc.realestate.models.entity.Property;
import com.dpdc.realestate.models.entity.User;
import com.dpdc.realestate.service.AminitieService;
import com.dpdc.realestate.service.HelperService;
import com.dpdc.realestate.service.MediaService;
import com.dpdc.realestate.service.PropertyService;

import com.dpdc.realestate.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/properties/")
@CrossOrigin
public class PropertyAPI {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private Environment env;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AminitieService aminitieService;

    @Autowired
    private Utils utils;

    @Autowired
    private HelperService helperService;

    @Autowired
    private MediaService mediaService;


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
        Page<Property> properties = propertyService.getProperties(propertyName,
                categoryId, priceFrom, priceTo, city,
                street, district, pageable);
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                properties), HttpStatus.OK);
    }

    @GetMapping("/my-property/{customerId}/")
    public ResponseEntity<ModelResponse> getMyProperty(@PathVariable Integer customerId,
                                                       @RequestParam(defaultValue = "1") String page){
//        PageRequest pageable = PageRequest.of(Integer.parseInt(page) - 1,
//                Integer.parseInt(env.getProperty("app.page.size")));
        Set<Property> properties = propertyService.findMyProperties(customerId);
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                properties), HttpStatus.OK);
    }


    @GetMapping("/my-property/{staffId}/staff/")
    public ResponseEntity<ModelResponse> getMyPropertyStaff(@PathVariable Integer staffId,
                                                       @RequestParam(defaultValue = "1") String page){
//        PageRequest pageable = PageRequest.of(Integer.parseInt(page) - 1,
//                Integer.parseInt(env.getProperty("app.page.size")));
        Set<Property> properties = propertyService.findMyPropertiesStaff(staffId);
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                properties), HttpStatus.OK);
    }


    @GetMapping("/{propertyId}/")
    public ResponseEntity<ModelResponse> getPropertyById(@PathVariable Integer propertyId){
        Property property = propertyService.getPropertyByIdAndDeleteFalse(propertyId);
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                property), HttpStatus.OK);
    }
    @GetMapping("/delete-property/")
    public ResponseEntity<ModelResponse> getDeletedProperty(){
        List<Property> properties = propertyService.getDeletedProperty();
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                properties), HttpStatus.OK);
    }
    @GetMapping("/get-all/")
    public ResponseEntity<ModelResponse> getAllProperty(){
        List<Property> properties = propertyService.getByIsActiveTrue();
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                properties), HttpStatus.OK);
    }
    @GetMapping("/not-active/")
    public ResponseEntity<ModelResponse> getPropertyNotActive(){
        Set<Property> properties = propertyService.findByIsActiveFalse();
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                properties), HttpStatus.OK);
    }

    @GetMapping("/unmanage/")
    public ResponseEntity<ModelResponse> getUnmanagedProperties(){
        List<Property> properties = propertyService.getUnmanagedProperties();
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                properties), HttpStatus.OK);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ModelResponse> registerProperty(@ModelAttribute AddProperty property)
           {
        try {
            Property mappedPropery = mapper.map(property, Property.class);
            mappedPropery.setDocuments(null);
            mappedPropery.setMedias(null);
            mappedPropery.setAminities(null);
            Property saveProperty = propertyService.addProperty(mappedPropery);
            Set<Aminitie> aminities = new HashSet<>();
            // add aminitie
            for (String name: property.getAminities()){
                Aminitie aminitie = new Aminitie();
                aminitie.setProperty(saveProperty);
                aminitie.setName(name);
                aminities.add(aminitie);
            }
            aminitieService.saveAll(aminities);
            return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                    saveProperty), HttpStatus.OK);
        }
        catch (AccountActiveException e){
            throw e;
        }
        catch (Exception exception) {
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


    @PostMapping("/assign/{propertyId}/{staffId}/")
    public ResponseEntity<ModelResponse> assignPropertyv2(@PathVariable Integer propertyId, @PathVariable Integer staffId){
        propertyService.assignProperty(propertyId, staffId);
        // hard code
        helperService.sendMessage("+84334436231", "Checkout new property");
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                null), HttpStatus.OK);
    }

    @DeleteMapping("/assign/{propertyId}/{staffId}/")
    public ResponseEntity<ModelResponse> deleteAssign(@PathVariable Integer propertyId, @PathVariable Integer staffId){
        propertyService.deleteAssign(propertyId, staffId);
        helperService.sendMessage("+84334436231", "cancel property");
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                null), HttpStatus.OK);
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
    public ResponseEntity<ModelResponse> updateProperty(@RequestBody AddProperty property,
                                                        @PathVariable Integer propertyId
            , BindingResult result) throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
//        Property myProperty = mapper.map(property ,Property.class);
        Property existProperty = propertyService.findById(propertyId);
        mapper.map(property, existProperty);
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


    @PostMapping("/active/{propertyId}/")
    public ResponseEntity<ModelResponse> activeProperty( @RequestBody Map<String,Boolean> mapIsActive,@PathVariable Integer propertyId
             ) throws Exception {
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

    @PostMapping("/upload-media/{propertyId}/")
    public ResponseEntity<ModelResponse> updateMedia(@ModelAttribute("media") MultipartFile media
    , @PathVariable Integer propertyId) throws IOException {
        Property p = new Property(propertyId);
        helperService.uploadImages(media,p);
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                null), HttpStatus.OK);
    }

    @PostMapping("/upload-document/{propertyId}/")
    public ResponseEntity<ModelResponse> updateDocument(@ModelAttribute("media") MultipartFile media , @PathVariable Integer propertyId) throws IOException {
        Property p = new Property(propertyId);
        helperService.uploadDocument(media,p);
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                null), HttpStatus.OK);
    }

    @DeleteMapping("/delete-media/{mediaId}/")
    public ResponseEntity<ModelResponse> deleteMedia( @PathVariable Integer mediaId)  {
        helperService.deleteMedia(mediaId);
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                null), HttpStatus.OK);
    }

    @DeleteMapping("/delete-document/{documentId}/")
    public ResponseEntity<ModelResponse> deleteDocument( @PathVariable Integer documentId)  {
        helperService.deleteDocument(documentId);
        return new ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                null), HttpStatus.OK);
    }
}
