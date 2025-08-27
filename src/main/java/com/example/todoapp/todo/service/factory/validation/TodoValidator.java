package com.example.todoapp.todo.service.factory.validation;

import org.springframework.stereotype.Component;

import com.example.todoapp.todo.dto.TodoRequest;
import com.example.todoapp.todo.exception.TodoFieldValidationException;

/**
 * {@link TodoRequest} のフィールドを検証するバリデータクラス
 */
@Component
public class TodoValidator {

    /**
     * {@link TodoRequest} のフィールドが {@code null} ではないことを検証する。
     *
     * @param field      検証対象のフィールド名
     * @param fieldValue 検証対象のフィールドの値
     * @return
     * @throws TodoFieldValidationException {@link TodoRequest} のフィールドの検証に失敗した場合
     */
    public void assertNotNull(String field, String fieldValue) {
        if (fieldValue == null) {
            throw new TodoFieldValidationException(
                field,
                fieldValue,
                "Field '%s' must not be null.".formatted(field)
            );
        }
    }

    /**
     * {@link TodoRequest} のフィールドが {@code blank} ではないことを検証する。
     *
     * @param field      検証対象のフィールド名
     * @param fieldValue 検証対象のフィールドの値
     * @return
     * @throws TodoFieldValidationException {@link TodoRequest} のフィールドの検証に失敗した場合
     */
    public void assertNotBlank(String field, String fieldValue) {
        if (fieldValue.isBlank()) {
            throw new TodoFieldValidationException(
                field,
                fieldValue,
                "Field '%s' must not be blank.".formatted(field)
            );
        }
    }

    /**
     * {@link TodoRequest} のフィールドが最大文字数を超えないことを検証する。
     *
     * @param field      検証対象のフィールド名
     * @param fieldValue 検証対象のフィールドの値
     * @param maxLength  最大文字数
     * @return
     * @throws TodoFieldValidationException {@link TodoRequest} のフィールドの検証に失敗した場合
     */
    public void assertMaxLength(String field, String fieldValue, int maxLength) {
        if (fieldValue.length() > maxLength) {
            throw new TodoFieldValidationException(
                field,
                fieldValue,
                "Field '%s' must not exceed %d characters.".formatted(field, maxLength)
            );
        }
    }
}
