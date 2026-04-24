package com.pixel.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GmailValidator.class)
public @interface Gmail {
    String message() default "Email must be Gmail";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
