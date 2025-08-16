package com.example.todoapp.common.exception;

/**
 * リクエストパラメータの例外処理で異常が発生した場合の例外
 */
public final class InvalidValidationException extends IllegalStateException {

    private static final long serialVersionUID = 1L;

    /** エラーメッセージ */
    private static final String ERROR_MESSAGE = "Request Exception Failure";

    /** コンテキストの識別情報 */
    private final String context;

    /** 例外の理由 */
    private final String reason;

    /**
     * リクエストパラメータの例外処理で異常が発生した場合の例外を生成する。
     *
     * @param context コンテキストの識別情報
     * @param reason  例外の理由
     */
    public InvalidValidationException(String context, String reason) {
        super(ERROR_MESSAGE);

        this.context = context;
        this.reason = reason;
    }

    /**
     * リクエストパラメータの例外処理で異常が発生した場合の例外を生成する。
     *
     * @param context コンテキストの識別情報
     * @param reason  例外の理由
     * @param cause   原因の例外
     */
    public InvalidValidationException(String context, String reason, Throwable cause) {
        super(ERROR_MESSAGE, cause);

        this.context = context;
        this.reason = reason;
    }

    /**
     * リクエストパラメータの例外処理で異常が発生した場合の詳細情報を返す。
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
