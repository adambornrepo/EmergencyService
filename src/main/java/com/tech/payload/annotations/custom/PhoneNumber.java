package com.tech.payload.annotations.custom;

import com.tech.payload.annotations.validator.PhoneNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {
    String message() default "This phone number is not valid (Valid Example:+xx-xxx-xxx-xxxx)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
