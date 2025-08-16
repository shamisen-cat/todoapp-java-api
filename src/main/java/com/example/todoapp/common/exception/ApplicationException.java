package com.example.todoapp.common.exception;

import com.example.todoapp.common.error.ErrorCode;

/**
 * アプリケーションの例外基底クラス
 */
public abstract class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** エラーコード */
    private final ErrorCode errorCode;

    /**
     * アプリケーションの例外を生成する。
     *
     * @param errorCode エラーコード
     * @param message   エラーメッセージ
     */
    protected ApplicationException(ErrorCode errorCode, String message) {
        super(message);

        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
