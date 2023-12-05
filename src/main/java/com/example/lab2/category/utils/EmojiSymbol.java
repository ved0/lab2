package com.example.lab2.category.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = EmojiSymbolValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EmojiSymbol {
    String message() default "Must be an emoji!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
