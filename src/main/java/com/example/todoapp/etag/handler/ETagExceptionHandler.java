package com.example.todoapp.etag.handler;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;

import com.example.todoapp.common.error.ErrorCode;
import com.example.todoapp.common.error.builder.ExceptionResponseBuilder;
import com.example.todoapp.etag.exception.ETagGenerationException;
import com.example.todoapp.etag.exception.ETagMismatchException;
import com.example.todoapp.etag.exception.ETagMissingException;

/**
 * ETagに関するエラーハンドラクラス
 * <p>
 * クライアントに {@link ProblemDetail} とHTTPステータスを返却する。
 */
@RestControllerAdvice(basePackages = "com.example.todoapp")
@RequiredArgsConstructor
public class ETagExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(
        ETagExceptionHandler.class
    );

    private final ExceptionResponseBuilder exceptionResponseBuilder;

    /**
     * ETagが {@code null} または {@code blank} の場合の例外処理
     *
     * @param ex          {@link ETagMissingException}
     * @param httpRequest HTTPリクエスト情報
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     */
    @ExceptionHandler(ETagMissingException.class)
    public ResponseEntity<ProblemDetail> handleETagMissing(
        ETagMissingException ex,
        HttpServletRequest httpRequest
    ) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorCode errorCode = ex.getErrorCode();

        logger.warn("[{}] {}", errorCode, ex.toString());

        return exceptionResponseBuilder.build(
            httpStatus,
            errorCode,
            ex.getErrorTitle(),
            httpRequest
        );
    }

    /**
     * ETagの整合性の検証に失敗した場合の例外処理
     *
     * @param ex          {@link ETagMismatchException}
     * @param httpRequest HTTPリクエスト情報
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     */
    @ExceptionHandler(ETagMismatchException.class)
    public ResponseEntity<ProblemDetail> handleETagMismatch(
        ETagMismatchException ex,
        HttpServletRequest httpRequest
    ) {
        HttpStatus httpStatus = HttpStatus.PRECONDITION_FAILED;
        ErrorCode errorCode = ex.getErrorCode();

        logger.warn("[{}] {}", errorCode, ex.toString());

        return exceptionResponseBuilder.build(
            httpStatus,
            errorCode,
            ex.getErrorTitle(),
            httpRequest
        );
    }

    /**
     * ETagの生成に失敗した場合の例外処理
     *
     * @param ex          {@link ETagGenerationException}
     * @param httpRequest HTTPリクエスト情報
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     */
    @ExceptionHandler(ETagGenerationException.class)
    public ResponseEntity<ProblemDetail> handleETagGeneration(
        ETagGenerationException ex,
        HttpServletRequest httpRequest
    ) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorCode errorCode = ex.getErrorCode();

        logger.error("[{}] {}", errorCode, ex.toString());

        return exceptionResponseBuilder.build(
            httpStatus,
            errorCode,
            ex.getErrorTitle(),
            httpRequest
        );
    }
}
