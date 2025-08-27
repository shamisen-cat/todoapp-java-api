package com.example.todoapp.common.error.builder.factory;

import java.net.URI;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

import com.example.todoapp.common.error.ErrorCode;

/**
 * {@link ProblemDetail} を生成するファクトリクラス
 */
@Component
public class ProblemDetailFactory {

    /**
     * {@link ProblemDetail} を生成する。
     *
     * @param httpStatus  HTTPステータス
     * @param errorCode   エラーコード
     * @param errorTitle  エラータイトル
     * @param httpRequest HTTPリクエスト情報
     * @param messageArgs {@link ErrorCode#messageTemplate} の引数
     * @return {@link ProblemDetail}
     */
    public ProblemDetail create(
        HttpStatus httpStatus,
        ErrorCode errorCode,
        String errorTitle,
        HttpServletRequest httpRequest,
        Object... messageArgs
    ) {
        ProblemDetail result = ProblemDetail.forStatus(httpStatus);
        result.setProperty("errorCode", errorCode.getErrorCode());
        result.setTitle(errorTitle);
        result.setDetail(errorCode.getMessageTemplate().formatted(messageArgs));
        result.setInstance(URI.create(httpRequest.getRequestURI()));

        return result;
    }
}
