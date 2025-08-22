package com.example.todoapp.etag.exception;

/**
 * ETagに関する例外基底クラス
 */
public class ETagException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * ETagに関する例外を生成する。
     *
     * @param message エラーメッセージ
     */
    public ETagException(String message) {
        super(message);
    }

    /**
     * ETagに関する例外を生成する。
     *
     * @param message エラーメッセージ
     * @param cause   原因の例外
     */
    public ETagException(String message, Throwable cause) {
        super(message, cause);
    }
}
