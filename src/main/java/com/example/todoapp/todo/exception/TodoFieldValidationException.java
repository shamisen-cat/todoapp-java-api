package com.example.todoapp.todo.exception;

import lombok.Getter;

import com.example.todoapp.common.error.ErrorCode;
import com.example.todoapp.todo.dto.TodoRequest;

/**
 * {@link TodoRequest} のフィールドの検証に失敗した場合の例外クラス
 */
@Getter
public class TodoFieldValidationException extends TodoException {

    private static final long serialVersionUID = 1L;

    /** エラータイトル */
    private static final String ERROR_TITLE = "Invalid To-do Field '%s'";

    /** フィールド名 */
    private final String field;

    /** フィールドの値 */
    private final String fieldValue;

    /** 例外の理由 */
    private final String reason;

    /**
     * {@link TodoRequest} のフィールドの検証に失敗した場合の例外を生成する。
     *
     * @param field      フィールド名
     * @param fieldValue フィールドの値
     * @param reason     例外の理由
     */
    public TodoFieldValidationException(
        String field,
        String fieldValue,
        String reason
    ) {
        super(ErrorCode.INVALID_TODO_FIELD, ERROR_TITLE.formatted(field));
        this.field = field;
        this.fieldValue = fieldValue;
        this.reason = reason;
    }

    /**
     * {@link TodoRequest} のフィールドの検証に失敗した場合の詳細情報を返す。
     *
     * @return 例外の詳細情報
     */
    @Override
    public String toString() {
        return "%s: %s [field=%s, fieldValue=%s, reason=%s]"
            .formatted(
                getClass().getSimpleName(),
                getErrorTitle(),
                getField(),
                getFieldValue(),
                getReason()
            );
    }
}
