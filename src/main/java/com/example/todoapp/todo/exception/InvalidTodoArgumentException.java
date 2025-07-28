package com.example.todoapp.todo.exception;

import com.example.todoapp.common.error.ErrorCode;
import com.example.todoapp.common.error.service.ProblemDetailInfo;
import com.example.todoapp.common.exception.ApplicationException;

/**
 * ToDoで引数が無効と判断された場合にスローされる例外クラス
 */
public final class InvalidTodoArgumentException extends ApplicationException
    implements ProblemDetailInfo {

    private static final long serialVersionUID = 1L;

    /**
     * コンテキストの識別情報
     */
    private final String context;

    /**
     * 引数名
     */
    private final String argName;

    /**
     * 引数が無効と判断された理由
     */
    private final String reason;

    public InvalidTodoArgumentException(
        String context,
        String argName,
        String reason
    ) {
        super(ErrorCode.INVALID_TODO_ARGUMENT);
        this.context = context;
        this.argName = argName;
        this.reason = reason;
    }

    /**
     * エラーメッセージの引数の配列を返す。
     *
     * @return エラーメッセージの引数の配列
     */
    @Override
    public Object[] getMessageArgs() {
        return new Object[] { getContext(), getArgName(), getReason() };
    }

    public String getContext(){
        return context;
    }

    public String getArgName() {
        return argName;
    }

    public String getReason() {
        return reason;
    }
}
