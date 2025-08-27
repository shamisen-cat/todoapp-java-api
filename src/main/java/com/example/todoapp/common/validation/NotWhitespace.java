package com.example.todoapp.common.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * 空白文字のバリデーションアノテーション
*/
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotWhitespaceValidator.class)
public @interface NotWhitespace {

    String message() default "{validation.notWhitespace}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
