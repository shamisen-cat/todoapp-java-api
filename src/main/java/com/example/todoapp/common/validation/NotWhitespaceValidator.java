package com.example.todoapp.common.validation;

import java.util.Optional;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 空白文字のバリデータクラス
 */
public final class NotWhitespaceValidator
    implements ConstraintValidator<NotWhitespace, String> {

    /**
     * 空白文字の検証をする。
     *
     * @param value   検証対象の文字列
     * @param context バリデーションコンテキスト
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Optional
            .ofNullable(value)
            .map(v -> v
                .chars()
                .anyMatch(c -> !Character.isWhitespace(c))
            )
            .orElse(true);
    }
}
