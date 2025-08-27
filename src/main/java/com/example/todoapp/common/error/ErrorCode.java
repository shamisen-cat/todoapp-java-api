package com.example.todoapp.common.error;

import org.springframework.http.ProblemDetail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.example.todoapp.todo.dto.TodoRequest;

/**
 * エラーコード設定
 * <p>
 * エラーコードと {@link ProblemDetail#detail} に設定するメッセージテンプレートを定義
 */
@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    /**
     * リクエストパラメータの検証に失敗した場合のエラーコード
     * <ul>
     *   <li>%s - フィールド名</li>
     * </ul>
     */
    REQUEST_VALIDATION_FAILURE(
        "REQUEST-400",
        "Request validation failed for field '%s'."
    ),

    /**
     * {@link TodoRequest} のフィールドの検証に失敗した場合のエラーコード
     * <ul>
     *   <li>%s - フィールド名</li>
     *   <li>%s - 例外の理由</li>
     * </ul>
     */
    INVALID_TODO_FIELD(
        "TODO-400-FIELD",
        "Invalid to-do field '%s': %s"
    ),

    /**
     * ETagが {@code null} または {@code blank} の場合のエラーコード
     */
    ETAG_MISSING(
        "ETAG-400-MISSING",
        "ETag is missing."
    ),

    /**
     * 指定されたIDのTo-doが存在しない場合のエラーコード
     */
    TODO_NOT_FOUND(
        "TODO-404",
        "To-do with the specified ID does not exist."
    ),

    /**
     * ETagの整合性の検証に失敗した場合のエラーコード
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
     * リクエストパラメータの検証に関する例外処理で問題が発生した場合のエラーコード
     */
    VALIDATION_HANDLING_FAILURE(
        "VALIDATION-500-HANDLING",
        "An unexpected error occurred while handling request validation."
    ),

    /**
     * 想定外の例外が発生した場合のエラーコード
     */
    INTERNAL_SERVER_ERROR(
        "SYS-500",
        "An unexpected internal server error occurred."
    );

    /** エラーコードの文字列識別子 */
    private final String errorCode;

    /** {@link ProblemDetail#detail} に設定するメッセージテンプレート */
    private final String messageTemplate;

    /**
     * エラーコードの文字列識別子を返す。
     *
     * @return エラーコードの文字列識別子
     */
    @Override
    public String toString() {
        return getErrorCode();
    }
}
