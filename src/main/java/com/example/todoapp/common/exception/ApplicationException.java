package com.example.todoapp.common.exception;

import com.example.todoapp.common.error.ErrorCode;

/**
 * アプリケーションの例外基底クラス
 */
public abstract class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 例外に対応するエラーコード
     */
    private final ErrorCode errorCode;

    protected ApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
