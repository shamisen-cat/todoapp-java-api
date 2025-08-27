package com.example.todoapp.common.error.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.RequiredArgsConstructor;

import com.example.todoapp.common.error.ErrorCode;
import com.example.todoapp.common.error.builder.ExceptionResponseBuilder;
import com.example.todoapp.common.error.handler.dto.FieldInfo;
import com.example.todoapp.common.error.handler.exception.ValidationHandlingException;
import com.example.todoapp.common.error.handler.util.FieldExtractor;

/**
 * グローバルエラーハンドラクラス
 * <p>
 * クライアントに {@link ProblemDetail} とHTTPステータスを返却する。
 */
@RestControllerAdvice(basePackages = "com.example.todoapp")
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final String REQUEST_VALIDATION_ERROR_TITLE = "Request Validation Failure";
    private static final String INTERNAL_SERVER_ERROR_TITLE = "Internal Server Error";

    private static final Logger logger = LoggerFactory.getLogger(
        GlobalExceptionHandler.class
    );

    private final FieldExtractor fieldExtractor;
    private final ExceptionResponseBuilder exceptionResponseBuilder;

    /**
     * メソッド引数の {@code @Valid} に失敗した場合の例外処理
     *
     * @param ex          {@link MethodArgumentNotValidException}
     * @param httpRequest HTTPリクエスト情報
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpServletRequest httpRequest
    ) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorCode errorCode = ErrorCode.REQUEST_VALIDATION_FAILURE;
        FieldInfo fieldInfo = fieldExtractor.from(ex);

        logger.warn("[{}] {}", errorCode, fieldInfo.logMessage());

        return exceptionResponseBuilder.build(
            httpStatus,
            errorCode,
            REQUEST_VALIDATION_ERROR_TITLE,
            httpRequest,
            fieldInfo.field()
        );
    }

    /**
     * メソッド引数の {@code @Validated} に失敗した場合の例外処理
     *
     * @param ex          {@link HandlerMethodValidationException}
     * @param httpRequest HTTPリクエスト情報
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ProblemDetail> handleHandlerMethodValidation(
        HandlerMethodValidationException ex,
        HttpServletRequest httpRequest
    ) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorCode errorCode = ErrorCode.REQUEST_VALIDATION_FAILURE;
        FieldInfo fieldInfo = fieldExtractor.from(ex);

        logger.warn("[{}] {}", errorCode, fieldInfo.logMessage());

        return exceptionResponseBuilder.build(
            httpStatus,
            errorCode,
            REQUEST_VALIDATION_ERROR_TITLE,
            httpRequest,
            fieldInfo.field()
        );
    }

    /**
     * Bean Validation の制約違反でバリデーションに失敗した場合の例外処理
     *
     * @param ex          {@link ConstraintViolationException}
     * @param httpRequest HTTPリクエスト情報
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(
        ConstraintViolationException ex,
        HttpServletRequest httpRequest
    ) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorCode errorCode = ErrorCode.REQUEST_VALIDATION_FAILURE;
        FieldInfo fieldInfo = fieldExtractor.from(ex);

        logger.warn("[{}] {}", errorCode, fieldInfo.logMessage());

        return exceptionResponseBuilder.build(
            httpStatus,
            errorCode,
            REQUEST_VALIDATION_ERROR_TITLE,
            httpRequest,
            fieldInfo.field()
        );
    }

    /**
     * リクエストパラメータの型変換に失敗した場合の例外処理
     *
     * @param ex          {@link MethodArgumentTypeMismatchException}
     * @param httpRequest HTTPリクエスト情報
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatch(
        MethodArgumentTypeMismatchException ex,
        HttpServletRequest httpRequest
    ) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorCode errorCode = ErrorCode.REQUEST_VALIDATION_FAILURE;

        logger.warn("[{}] {}", errorCode, ex.getMessage());

        return exceptionResponseBuilder.build(
            httpStatus,
            errorCode,
            REQUEST_VALIDATION_ERROR_TITLE,
            httpRequest,
            ex.getName()
        );
    }

    /**
     * リクエストパラメータの検証に関する例外処理で問題が発生した場合の例外処理
     *
     * @param ex          {@link ValidationHandlingException}
     * @param httpRequest HTTPリクエスト情報
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     */
    @ExceptionHandler(ValidationHandlingException.class)
    public ResponseEntity<ProblemDetail> handleValidationHandling(
        ValidationHandlingException ex,
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

    /**
     * 想定外の例外処理
     *
     * @param ex          想定外の例外
     * @param httpRequest HTTPリクエスト情報
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpectedException(
        Exception ex,
        HttpServletRequest httpRequest
    ) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        logger.error("[{}]", errorCode, ex);

        return exceptionResponseBuilder.build(
            httpStatus,
            errorCode,
            INTERNAL_SERVER_ERROR_TITLE,
            httpRequest
        );
    }
}
