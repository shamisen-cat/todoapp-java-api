package com.example.todoapp.todo.exception;

import java.util.UUID;

import com.example.todoapp.common.error.ErrorCode;
import com.example.todoapp.common.error.factory.ProblemDetailInfo;
import com.example.todoapp.common.exception.ApplicationException;

/**
 * 指定されたIDのToDoが見つからなかった場合にスローされる例外クラス
 */
public final class TodoEntityNotFoundException extends ApplicationException
    implements ProblemDetailInfo {

    private static final long serialVersionUID = 1L;

    /**
     * ToDoのID
     */
    private final UUID id;

    public TodoEntityNotFoundException(UUID id) {
        super(ErrorCode.NOT_FOUND_TODO);
        this.id = id;
    }

    /**
     * エラーメッセージの引数の配列を返す。
     *
     * @return エラーメッセージの引数の配列
     */
    @Override
    public Object[] getMessageArgs() {
        return new Object[] { getId() };
    }

    public UUID getId() {
        return id;
    }
}
