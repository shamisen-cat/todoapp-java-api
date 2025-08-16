package com.example.todoapp.todo.exception;

import com.example.todoapp.common.error.ErrorCode;
import com.example.todoapp.common.exception.ApplicationException;
import com.example.todoapp.todo.model.Todo;

/**
 * {@link Todo} のタイトルの検証に失敗した場合の例外
 */
public final class TodoTitleValidationException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    /** エラーメッセージ */
    private static final String ERROR_MESSAGE = "Invalid Todo Title";

    /** コンテキストの識別情報 */
    private final String context;

    /** 検証対象のタイトル */
    private final String title;

    /** 例外の理由 */
    private final String reason;

    /**
     * {@link Todo} のタイトルの検証に失敗した場合の例外を生成する。
     *
     * @param context コンテキストの識別情報
     * @param title   検証対象のタイトル
     * @param reason  例外の理由
     */
    public TodoTitleValidationException(String context, String title, String reason) {
        super(ErrorCode.INVALID_TODO_TITLE, ERROR_MESSAGE);

        this.context = context;
        this.title = title;
        this.reason = reason;
    }

    /**
     * {@link Todo} のタイトルの検証に失敗した場合の詳細情報を返す。
     *
     * @return 例外の詳細情報
     */
    @Override
    public String toString() {
        return "%s: %s [context=%s, title=%s, reason=%s]"
            .formatted(
                getClass().getSimpleName(),
                getMessage(),
                getContext(),
                getTitle(),
                getReason()
            );
    }

    public String getContext(){
        return context;
    }

    public String getTitle() {
        return title;
    }

    public String getReason() {
        return reason;
    }
}
