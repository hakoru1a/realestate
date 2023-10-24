package com.dpdc.realestate.apis;


import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.exception.BodyBadRequestException;
import com.dpdc.realestate.exception.DataAlreadyExistException;
import com.dpdc.realestate.exception.NotFoundException;
import com.dpdc.realestate.models.entity.Package;
import com.dpdc.realestate.service.PackageService;
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
import java.util.Set;

@RestController
@RequestMapping("/api/packages/")
public class PackageAPI {

    @Autowired
    private Environment env;

    @Autowired
    private PackageService packageService;
    @GetMapping
    public ResponseEntity<ModelResponse> getPackages(@RequestParam(defaultValue = "1") String page){
        Pageable pageable = PageRequest.of( Integer.parseInt(page) - 1,
                Integer.parseInt(env.getProperty("app.page.size")));
        Page<Package> packages = packageService.getPackages(pageable);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), packages),
                HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<ModelResponse> createPackage(@RequestBody @Valid Package pack,
                                                    BindingResult result) throws Exception {
        try{
            if (result.hasErrors()) {
                throw new BindException(result);
            }
            Package savedPackage = packageService.addPackage(pack);
            return new
                    ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), savedPackage),
                    HttpStatus.OK);
        }
        catch (HttpMessageNotReadableException | BindException | DataAlreadyExistException e){
            throw e;
        }
        catch (Exception ex){
            throw new Exception(env.getProperty("db.notify.save_fail"));
        }
    }

    @DeleteMapping("/{packageId}/")
    public ResponseEntity<ModelResponse> deletePackage(@PathVariable Integer packageId) throws Exception {
        try{
            packageService.deletePackage(packageId);
            return new
                    ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), null),
                    HttpStatus.NO_CONTENT);
        }
        catch (NotFoundException notFoundException){
            throw  notFoundException;
        }
        catch (Exception ex){
            throw new Exception(env.getProperty("db.notify.delete_fail"));
        }
    }

    @PutMapping("/{packageId}/")
    public ResponseEntity<ModelResponse> updatePackage(
            @PathVariable Integer packageId,
            @RequestBody @Valid Package pack,
            BindingResult result
    ) throws Exception {
        try{
            if (result.hasErrors()) {
                throw new BindException(result);
            }
            Package savedPackage = packageService.updatePackage(pack, packageId);
            return new
                    ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"), savedPackage),
                    HttpStatus.OK);
        }
        catch (HttpMessageNotReadableException |
               BindException |
               BodyBadRequestException | NotFoundException e){
            throw e;
        }
        catch (Exception ex){
            throw new Exception(env.getProperty("db.notify.update_fail"));
        }
    }


}
