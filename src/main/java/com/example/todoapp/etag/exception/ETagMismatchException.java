package com.example.todoapp.etag.exception;

import com.example.todoapp.common.error.ErrorCode;

import lombok.Getter;

/**
 * ETagの整合性の検証に失敗した場合の例外クラス
 */
@Getter
public final class ETagMismatchException extends ETagException {

    private static final long serialVersionUID = 1L;

    /** エラータイトル */
    private static final String ERROR_TITLE = "ETag Mismatch";

    /** 検証対象のETag文字列値 */
    private final String eTag;

    /** 比較対象のETag文字列値 */
    private final String expected;

    /**
     * ETagの整合性の検証に失敗した場合の例外を生成する。
     *
     * @param eTag     検証対象のETag文字列値
     * @param expected 比較対象のETag文字列値
     */
    public ETagMismatchException(String eTag, String expected) {
        super(ErrorCode.ETAG_MISMATCH, ERROR_TITLE);
        this.eTag = eTag;
        this.expected = expected;
    }

    /**
     * ETagの整合性の検証に失敗した場合の詳細情報を返す。
     *
     * @return 例外の詳細情報
     */
    @Override
    public String toString() {
        return "%s: %s [eTag=%s, expected=%s]"
            .formatted(
                getClass().getSimpleName(),
                getErrorTitle(),
                getETag(),
                getExpected()
            );
    }
}
