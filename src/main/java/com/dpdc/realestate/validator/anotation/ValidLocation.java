package com.dpdc.realestate.validator.anotation;

import com.dpdc.realestate.validator.LocationValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LocationValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLocation {
    String message() default "location Invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}