package com.example.todoapp.common.error;

/**
 * 例外に対応するエラーコードとエラーメッセージを定義する列挙型
 */
public enum ErrorCode {

    /**
     * ToDoで引数が無効と判断された場合のエラーコード
     * <p>
     * %s - コンテキストの識別情報
     * %s - 引数名
     * %s - 引数が無効と判断された理由
     */
    INVALID_TODO_ARGUMENT(
        "TODO-400-ARG",
        "%s: Argument %s is invalid. Reason: %s"
    ),

    /**
     * 指定されたIDのToDoが見つからなかった場合のエラーコード
     * <p>
     * %s - ToDoのID
     */
    NOT_FOUND_TODO(
        "TODO-404",
        "ToDo with ID '%s' was not found."
    ),

    /**
     * リクエストパラメータの検証に失敗した場合のエラーコード
     * <p>
     * %s - フィールド名
     * %s - エラーメッセージ
     */
    VALIDATION_FAILURE(
        "SYS-400-VALIDATION",
        "Validation failed for field '%s': %s"
    ),

    /**
     * ETagの検証に失敗した場合のエラーコード
     * <p>
     * %s - コンテキストの識別情報
     * %s - クライアントから受信したIf-Match
     * %s - サーバ側のETag
     */
    ETAG_MISMATCH(
        "SYS-412-ETAG",
        "%s: ETag mismatch. If-Match = '%s', but current ETag = '%s'."
    ),

    /**
     * ETagの生成に失敗した場合のエラーコード
     * <p>
     * %s - コンテキストの識別情報
     * %s - 例外理由
     */
    ETAG_GENERATION_FAILURE(
        "SYS-500-ETAG",
        "%s: Failed to generate ETag. Reason: %s"
    ),

    /**
     * 想定されていない例外が発生した場合のエラーコード
     */
    INTERNAL_SERVER_ERROR(
        "SYS-500",
        "An unexpected internal error has occurred."
    );

    /**
     * エラーコードの文字列識別子
     */
    private final String code;

    /**
     * エラーメッセージ
     */
    private final String message;

    private ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * プレースホルダに引数を埋め込み、整形されたエラーメッセージを返す。
     *
     * @param messageArgs エラーメッセージの引数の配列
     * @return 整形されたエラーメッセージ
     */
    public String formatMessage(Object... messageArgs) {
        return String.format(message, messageArgs);
    }
}
