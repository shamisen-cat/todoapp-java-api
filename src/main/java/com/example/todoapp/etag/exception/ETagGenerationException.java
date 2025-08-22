package com.example.todoapp.etag.exception;

/**
 * ETagの生成に失敗した場合の例外
 */
public final class ETagGenerationException extends ETagException {

    private static final long serialVersionUID = 1L;

    /** エラーメッセージ */
    private static final String ERROR_MESSAGE = "ETag Generation Failure";

    /** コンテキストの識別情報 */
    private final String context;

    /** 例外の理由 */
    private final String reason;

    /**
     * ETagの生成に失敗した場合の例外を生成する。
     *
     * @param context コンテキストの識別情報
     * @param reason  例外の理由
     */
    public ETagGenerationException(String context, String reason) {
        super(ERROR_MESSAGE);

        this.context = context;
        this.reason = reason;
    }

    /**
     * ETagの生成に失敗した場合の例外を生成する。
     *
     * @param context コンテキストの識別情報
     * @param reason  例外の理由
     * @param cause   原因の例外
     */
    public ETagGenerationException(String context, String reason, Throwable cause) {
        super(ERROR_MESSAGE, cause);

        this.context = context;
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

        return "%s: %s [context=%s, reason=%s]%s"
            .formatted(
                getClass().getSimpleName(),
                getMessage(),
                getContext(),
                getReason(),
                causeInfo
            );
    }

    public String getContext() {
        return context;
    }

    public String getReason() {
        return reason;
    }
}
