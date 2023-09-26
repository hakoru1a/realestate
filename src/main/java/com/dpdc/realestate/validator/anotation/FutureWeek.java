package com.dpdc.realestate.validator.anotation;

import com.dpdc.realestate.validator.FutureWeekValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureWeekValidator.class)
@Documented
public @interface FutureWeek {
    String message() default "Ngày hẹn phải là tuần sau.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}