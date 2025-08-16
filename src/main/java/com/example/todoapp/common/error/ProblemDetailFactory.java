package com.example.todoapp.common.error;

import java.net.URI;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

import com.example.todoapp.common.exception.ApplicationException;
import com.example.todoapp.etag.exception.ETagException;

/**
 * REST APIで返却する {@link ProblemDetail} を生成するファクトリ
 */
@Component
public class ProblemDetailFactory {

    /**
     * {@link ApplicationException} の {@link ProblemDetail} を生成する。
     *
     * @param status      HTTPステータス
     * @param ex          {@link ApplicationException}
     * @param request     リクエスト情報
     * @param messageArgs {@link ErrorCode#message} のフォーマットに使用する引数
     * @return {@link ProblemDetail}
     */
    public ProblemDetail create(
        HttpStatus status,
        ApplicationException ex,
        HttpServletRequest request,
        Object... messageArgs
    ) {
        ProblemDetail result = ProblemDetail.forStatus(status);

        result.setProperty("errorCode", ex.getErrorCode().getCode());
        result.setTitle(ex.getMessage());
        result.setDetail(ex.getErrorCode().getMessage().formatted(messageArgs));
        result.setInstance(URI.create(request.getRequestURI()));

        return result;
    }

    /**
     * {@link ETagException} の {@link ProblemDetail} を生成する。
     *
     * @param status      HTTPステータス
     * @param errorCode   エラーコード
     * @param ex          {@link ETagException}
     * @param request     リクエスト情報
     * @param messageArgs {@link ErrorCode#message} のフォーマットに使用する引数
     * @return {@link ProblemDetail}
     */
    public ProblemDetail create(
        HttpStatus status,
        ErrorCode errorCode,
        ETagException ex,
        HttpServletRequest request,
        Object... messageArgs
    ) {
        ProblemDetail result = ProblemDetail.forStatus(status);

        result.setProperty("errorCode", errorCode.getCode());
        result.setTitle(ex.getMessage());
        result.setDetail(errorCode.getMessage().formatted(messageArgs));
        result.setInstance(URI.create(request.getRequestURI()));

        return result;
    }

    /**
     * {@link ProblemDetail} を生成する。
     *
     * @param status      HTTPステータス
     * @param errorCode   エラーコード
     * @param title       {@link ProblemDetail#title}
     * @param request     リクエスト情報
     * @param messageArgs {@link ErrorCode#message} のフォーマットに使用する引数
     * @return {@link ProblemDetail}
     */
    public ProblemDetail create(
        HttpStatus status,
        ErrorCode errorCode,
        String title,
        HttpServletRequest request,
        Object... messageArgs
    ) {
        ProblemDetail result = ProblemDetail.forStatus(status);

        result.setProperty("errorCode", errorCode.getCode());
        result.setTitle(title);
        result.setDetail(errorCode.getMessage().formatted(messageArgs));
        result.setInstance(URI.create(request.getRequestURI()));

        return result;
    }
}
