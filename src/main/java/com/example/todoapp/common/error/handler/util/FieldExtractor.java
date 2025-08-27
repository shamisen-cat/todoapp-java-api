package com.example.todoapp.common.error.handler.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import com.example.todoapp.common.error.handler.dto.FieldInfo;
import com.example.todoapp.common.error.handler.exception.ValidationHandlingException;

/**
 * リクエストパラメータの検証に関する例外から {@link FieldInfo} を取り出すユーティリティクラス
 */
@Component
public class FieldExtractor {

    /**
     * {@link MethodArgumentNotValidException} から {@link FieldInfo} を取り出す。
     *
     * @param ex {@link MethodArgumentNotValidException}
     * @return {@link FieldInfo}
     */
    public FieldInfo from(MethodArgumentNotValidException ex) {
        FieldError first = ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .orElseThrow(() -> new ValidationHandlingException("FieldError is empty."));

        return new FieldInfo(first.getField(), first.getDefaultMessage());
    }

    /**
     * {@link HandlerMethodValidationException} から {@link FieldInfo} を取り出す。
     *
     * @param ex {@link HandlerMethodValidationException}
     * @return {@link FieldInfo}
     */
    public FieldInfo from(HandlerMethodValidationException ex) {
        ParameterValidationResult first = ex.getValueResults().stream()
            .findFirst()
            .orElseThrow(() -> new ValidationHandlingException(
                "ParameterValidationResult is empty."
            ));

        return new FieldInfo(
            first.getMethodParameter().getParameterName(),
            first.getResolvableErrors().get(0).getDefaultMessage()
        );
    }

    /**
     * {@link ConstraintViolationException} から {@link FieldInfo} を取り出す。
     *
     * @param ex {@link ConstraintViolationException}
     * @return {@link FieldInfo}
     */
    public FieldInfo from(ConstraintViolationException ex) {
        ConstraintViolation<?> first = ex.getConstraintViolations().stream()
            .findFirst()
            .orElseThrow(() -> new ValidationHandlingException(
                "ConstraintViolation is empty."
            ));

        return new FieldInfo(first.getPropertyPath().toString(), first.getMessage());
    }
}
