package com.example.todoapp.etag.exception;

import lombok.Getter;

import com.example.todoapp.common.error.ErrorCode;

/**
 * ETagが {@code null} または {@code blank} の場合の例外クラス
 */
@Getter
public class ETagMissingException extends ETagException {

    private static final long serialVersionUID = 1L;

    /** エラータイトル */
    private static final String ERROR_TITLE = "ETag Missing";

    /** 検証対象のETag文字列値 */
    private final String eTag;

    /**
     * ETagが {@code null} または {@code blank} の場合の例外を生成する。
     *
     * @param eTag 検証対象のETag文字列値
     */
    public ETagMissingException(String eTag) {
        super(ErrorCode.ETAG_MISSING, ERROR_TITLE);
        this.eTag = eTag;
    }

    /**
     * ETagが {@code null} または {@code blank} の場合の詳細情報を返す。
     *
     * @return 例外の詳細情報
     */
    @Override
    public String toString() {
        return "%s: %s [eTag=%s]"
            .formatted(
                getClass().getSimpleName(),
                getErrorTitle(),
                getETag()
            );
    }
}
