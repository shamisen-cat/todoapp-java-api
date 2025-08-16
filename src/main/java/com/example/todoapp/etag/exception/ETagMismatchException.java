package com.example.todoapp.etag.exception;

/**
 * ETagの整合性検証に失敗した場合の例外
 */
public final class ETagMismatchException extends ETagException {

    private static final long serialVersionUID = 1L;

    /** エラーメッセージ */
    private static final String ERROR_MESSAGE = "ETag Mismatch";

    /** コンテキストの識別情報 */
    private final String context;

    /** 検証対象のETagの値 */
    private final String eTag;

    /** 比較対象のETagの値 */
    private final String expected;

    /**
     * ETagの整合性検証に失敗した場合の例外を生成する。
     *
     * @param context  コンテキストの識別情報
     * @param eTag     検証対象のETagの値
     * @param expected 比較対象のETagの値
     */
    public ETagMismatchException(String context, String eTag, String expected) {
        super(ERROR_MESSAGE);

        this.context = context;
        this.eTag = eTag;
        this.expected = expected;
    }

    /**
     * ETagの整合性検証に失敗した場合の例外を生成する。
     *
     * @param context  コンテキストの識別情報
     * @param eTag     検証対象のETagの値
     * @param expected 比較対象のETagの値
     * @param cause    原因の例外
     */
    public ETagMismatchException(
        String context,
        String eTag,
        String expected,
        Throwable cause
    ) {
        super(ERROR_MESSAGE, cause);

        this.context = context;
        this.eTag = eTag;
        this.expected = expected;
    }

    /**
     * ETagの整合性検証に失敗した場合の詳細情報を返す。
     *
     * @return 例外の詳細情報
     */
    @Override
    public String toString() {
        String causeInfo = getCause() != null
            ? " (Caused by: %s)".formatted(getCause())
            : "";

        return "%s: %s [context=%s, eTag=%s, expected=%s]%s"
            .formatted(
                getClass().getSimpleName(),
                getMessage(),
                getContext(),
                getETag(),
                getExpected(),
                causeInfo
            );
    }

    public String getContext() {
        return context;
    }

    public String getETag() {
        return eTag;
    }

    public String getExpected() {
        return expected;
    }
}
