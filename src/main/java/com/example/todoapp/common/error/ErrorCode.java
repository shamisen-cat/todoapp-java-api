package com.example.todoapp.common.error;

import org.springframework.http.ProblemDetail;

import com.example.todoapp.todo.model.Todo;

/**
 * エラーコードと {@link ProblemDetail#detail} に設定するメッセージを定義する列挙型
 */
public enum ErrorCode {

    /**
     * リクエストパラメータの検証に失敗した場合のエラーコード
     * <ul>
     *   <li>%s - フィールド名</li>
     * </ul>
     */
    REQUEST_VALIDATION_FAILURE(
        "REQUEST-400",
        "Request validation failed for field: %s"
    ),

    /**
     * {@link Todo} のタイトルの検証に失敗した場合のエラーコード
     * <ul>
     *   <li>%s - 例外の理由</li>
     * </ul>
     */
    INVALID_TODO_TITLE(
        "TODO-400-TITLE",
        "Invalid Todo Title: %s"
    ),

    /**
     * 指定されたIDの {@link Todo} が存在しない場合のエラーコード
     */
    TODO_NOT_FOUND(
        "TODO-404",
        "Todo with the specified ID does not exist."
    ),

    /**
     * ETagの整合性検証に失敗した場合のエラーコード
     */
    ETAG_MISMATCH(
        "ETAG-412",
        "The ETag does not match the expected value."
    ),

    /**
     * ETagの生成に失敗した場合のエラーコード
     */
    ETAG_GENERATION_FAILURE(
        "ETAG-500-GENERATION",
        "An unexpected error occurred while generating the ETag."
    ),

    /**
     * 想定外の例外が発生した場合のエラーコード
     */
    INTERNAL_SERVER_ERROR(
        "SYS-500",
        "An unexpected internal server error occurred."
    );

    /** エラーコードの文字列識別子 */
    private final String code;

    /** {@link ProblemDetail#detail} に設定するメッセージ */
    private final String message;

    /**
     * エラーコードと {@link ProblemDetail#detail} に設定するメッセージを生成する。
     *
     * @param code    エラーコードの文字列識別子
     * @param message {@link ProblemDetail#detail} に設定するメッセージ
     */
    private ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * エラーコードの文字列識別子を返す。
     *
     * @return エラーコードの文字列識別子
     */
    @Override
    public String toString() {
        return getCode();
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
