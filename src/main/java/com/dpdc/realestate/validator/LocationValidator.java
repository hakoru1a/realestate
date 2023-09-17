package com.dpdc.realestate.validator;

import com.dpdc.realestate.models.entity.Location;
import com.dpdc.realestate.validator.anotation.ValidLocation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LocationValidator implements ConstraintValidator<ValidLocation, Location> {

    @Override
    public void initialize(ValidLocation constraintAnnotation) {
        // Initialization code, if needed
    }
    @Override
    public boolean isValid(Location location, ConstraintValidatorContext context) {
        if (location == null) {
            return false; // Location cannot be null
        }
        // Check that city, district, and street are not null or empty
        return location.getCity() != null && !location.getCity().isEmpty() &&
                location.getDistrict() != null && !location.getDistrict().isEmpty() &&
                location.getStreet() != null && !location.getStreet().isEmpty();
    }
}
