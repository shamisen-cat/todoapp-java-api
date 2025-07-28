package com.example.todoapp.common.error.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

import com.example.todoapp.common.error.ErrorCode;
import com.example.todoapp.common.exception.ApplicationException;

/**
 * REST APIレスポンスで返却するエラー情報を生成するファクトリクラス
 */
@Component
public class ProblemDetailFactory {

    /**
     * 例外からエラー情報を生成する。
     *
     * @param status HTTPステータス
     * @param ex     発生した例外
     * @return REST APIレスポンスで返却するエラー情報
     */
    public ProblemDetail create(HttpStatus status, ApplicationException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        return (ex instanceof ProblemDetailInfo info)
            ? create(status, errorCode, info.getMessageArgs())
            : create(status, errorCode);
    }

    /**
     * HTTPステータスとエラーコードからエラー情報を生成する。
     *
     * @param status    HTTPステータス
     * @param errorCode エラーコード
     * @return REST APIレスポンスで返却するエラー情報
     */
    public ProblemDetail create(HttpStatus status, ErrorCode errorCode) {
        ProblemDetail result = ProblemDetail.forStatus(status);

        result.setTitle(errorCode.name());
        result.setDetail(errorCode.getMessage());
        result.setProperty("errorCode", errorCode.getCode());

        return result;
    }

    /**
     * HTTPステータスとエラーコードから整形されたエラーメッセージのエラー情報を生成する。
     *
     * @param status      HTTPステータス
     * @param errorCode   エラーコード
     * @param messageArgs エラーメッセージの引数の配列
     * @return REST APIレスポンスで返却するエラー情報
     */
    public ProblemDetail create(
        HttpStatus status,
        ErrorCode errorCode,
        Object... messageArgs
    ) {
        ProblemDetail result = ProblemDetail.forStatus(status);

        result.setTitle(errorCode.name());
        result.setDetail(errorCode.formatMessage(messageArgs));
        result.setProperty("errorCode", errorCode.getCode());

        return result;
    }
}
