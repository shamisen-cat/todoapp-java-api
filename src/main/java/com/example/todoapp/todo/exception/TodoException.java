package com.example.todoapp.todo.exception;

import org.springframework.http.ProblemDetail;

import lombok.Getter;

import com.example.todoapp.common.error.ErrorCode;

/**
 * To-doに関する例外基底クラス
 */
@Getter
public abstract class TodoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** エラーコード */
    private final ErrorCode errorCode;

    /**
     * To-doに関する例外を生成する。
     *
     * @param errorCode  エラーコード
     * @param errorTitle エラータイトル
     */
    protected TodoException(ErrorCode errorCode, String errorTitle) {
        super(errorTitle);
        this.errorCode = errorCode;
    }

    /**
     * To-doに関する例外を生成する。
     *
     * @param errorCode  エラーコード
     * @param errorTitle エラータイトル
     * @param cause      原因の例外
     */
    protected TodoException(ErrorCode errorCode, String errorTitle, Throwable cause) {
        super(errorTitle, cause);
        this.errorCode = errorCode;
    }

    /**
     * {@link ProblemDetail#title} に設定するエラータイトルを取得する。
     *
     * @return エラータイトル
     */
    public String getErrorTitle() {
        return getMessage();
    }
}
