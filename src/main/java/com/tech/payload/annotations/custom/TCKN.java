package com.tech.payload.annotations.custom;

import com.tech.payload.annotations.validator.TcknValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TcknValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TCKN {

    String message() default "This TCKN is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
