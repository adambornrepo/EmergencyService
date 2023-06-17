package com.tech.payload.annotations.custom;

import com.tech.payload.annotations.validator.SsnValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SsnValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SSN {

    String message() default "This SSN is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
