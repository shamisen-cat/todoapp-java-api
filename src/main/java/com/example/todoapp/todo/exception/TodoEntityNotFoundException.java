package com.example.todoapp.todo.exception;

import java.util.UUID;

import com.example.todoapp.common.error.ErrorCode;
import com.example.todoapp.common.exception.ApplicationException;
import com.example.todoapp.todo.model.Todo;

/**
 * 指定されたIDの {@link Todo} が存在しない場合の例外
 */
public final class TodoEntityNotFoundException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    /** エラーメッセージ */
    private static final String ERROR_MESSAGE = "Todo Not Found";

    /** コンテキストの識別情報 */
    private final String context;

    /** 検索対象の {@link Todo} のID */
    private final UUID todoId;

    /**
     * 指定されたIDの {@link Todo} が存在しない場合の例外を生成する。
     *
     * @param context コンテキストの識別情報
     * @param todoId  検索対象の {@link Todo} のID
     */
    public TodoEntityNotFoundException(String context, UUID todoId) {
        super(ErrorCode.TODO_NOT_FOUND, ERROR_MESSAGE);

        this.context = context;
        this.todoId = todoId;
    }

    /**
     * 指定されたIDの {@link Todo} が存在しない場合の詳細情報を返す。
     *
     * @return 例外の詳細情報
     */
    @Override
    public String toString() {
        return "%s: %s [context=%s, todoId=%s]"
            .formatted(
                getClass().getSimpleName(),
                getMessage(),
                getContext(),
                getTodoId()
            );
    }

    public String getContext() {
        return context;
    }

    public UUID getTodoId() {
        return todoId;
    }
}
