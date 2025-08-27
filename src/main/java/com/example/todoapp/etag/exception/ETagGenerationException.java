package com.example.todoapp.etag.exception;

import lombok.Getter;

import com.example.todoapp.common.error.ErrorCode;

/**
 * ETagの生成に失敗した場合の例外クラス
 */
@Getter
public final class ETagGenerationException extends ETagException {

    private static final long serialVersionUID = 1L;

    /** エラータイトル */
    private static final String ERROR_TITLE = "ETag Generation Failure";

    /** 例外の理由 */
    private final String reason;

    /**
     * ETagの生成に失敗した場合の例外を生成する。
     *
     * @param reason 例外の理由
     */
    public ETagGenerationException(String reason) {
        super(ErrorCode.ETAG_GENERATION_FAILURE, ERROR_TITLE);
        this.reason = reason;
    }

    /**
     * ETagの生成に失敗した場合の例外を生成する。
     *
     * @param reason 例外の理由
     * @param cause  原因の例外
     */
    public ETagGenerationException(String reason, Throwable cause) {
        super(ErrorCode.ETAG_GENERATION_FAILURE, ERROR_TITLE, cause);
        this.reason = reason;
    }

    /**
     * ETagの生成に失敗した場合の詳細情報を返す。
     *
     * @return 例外の詳細情報
     */
    @Override
    public String toString() {
        String causeInfo = getCause() != null
            ? " (Caused by: %s)".formatted(getCause())
            : "";

        return "%s: %s [reason=%s]%s"
            .formatted(
                getClass().getSimpleName(),
                getErrorTitle(),
                getReason(),
                causeInfo
            );
    }
}
