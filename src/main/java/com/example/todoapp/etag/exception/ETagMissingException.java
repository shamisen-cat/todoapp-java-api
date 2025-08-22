package com.example.todoapp.etag.exception;

/**
 * ETagが {@code null} または空文字の場合の例外
 */
public class ETagMissingException extends ETagException {

    private static final long serialVersionUID = 1L;

    /** エラーメッセージ */
    private static final String ERROR_MESSAGE = "ETag must not be null or blank.";

    /** コンテキストの識別情報 */
    private final String context;

    /** 検証対象のETagの値 */
    private final String eTag;

    /**
     * ETagが {@code null} または空文字の場合の例外を生成する。
     *
     * @param context コンテキストの識別情報
     * @param eTag    検証対象のETagの値
     */
    public ETagMissingException(String context, String eTag) {
        super(ERROR_MESSAGE);

        this.context = context;
        this.eTag = eTag;
    }

    /**
     * ETagが {@code null} または空文字の場合の例外を生成する。
     *
     * @param context コンテキストの識別情報
     * @param eTag    検証対象のETagの値
     * @param cause   原因の例外
     */
    public ETagMissingException(String context, String eTag, Throwable cause) {
        super(ERROR_MESSAGE, cause);

        this.context = context;
        this.eTag = eTag;
    }

    /**
     * ETagが {@code null} または空文字の場合の詳細情報を返す。
     *
     * @return 例外の詳細情報
     */
    @Override
    public String toString() {
        String causeInfo = getCause() != null
            ? " (Caused by: %s)".formatted(getCause())
            : "";

        return "%s: %s [context=%s, eTag=%s]%s"
            .formatted(
                getClass().getSimpleName(),
                getMessage(),
                getContext(),
                getETag(),
                causeInfo
            );
    }

    public String getContext() {
        return context;
    }

    public String getETag() {
        return eTag;
    }
}
