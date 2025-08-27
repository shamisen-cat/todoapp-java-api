package com.example.todoapp.common.error.handler.exception;

import org.springframework.http.ProblemDetail;

import lombok.Getter;

import com.example.todoapp.common.error.ErrorCode;

/**
 * リクエストパラメータの検証に関する例外処理で問題が発生した場合の例外クラス
 */
@Getter
public final class ValidationHandlingException extends IllegalStateException {

    private static final long serialVersionUID = 1L;

    /** エラータイトル */
    private static final String ERROR_TITLE = "Request Validation Handling Failure";

    /** エラーコード */
    private final ErrorCode errorCode = ErrorCode.VALIDATION_HANDLING_FAILURE;

    /** 例外の理由 */
    private final String reason;

    /**
     * リクエストパラメータの検証に関する例外処理で問題が発生した場合の例外を生成する。
     *
     * @param reason 例外の理由
     */
    public ValidationHandlingException(String reason) {
        super(ERROR_TITLE);
        this.reason = reason;
    }

    /**
     * リクエストパラメータの検証に関する例外処理で問題が発生した場合の詳細情報を返す。
     *
     * @return 例外の詳細情報
     */
    @Override
    public String toString() {
        return "%s: %s [reason=%s]"
            .formatted(
                getClass().getSimpleName(),
                getErrorTitle(),
                getReason()
            );
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
