package com.example.todoapp.todo.handler;

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
import com.example.todoapp.todo.exception.TodoEntityNotFoundException;
import com.example.todoapp.todo.exception.TodoFieldValidationException;

/**
 * To-doに関するエラーハンドラクラス
 * <p>
 * クライアントに {@link ProblemDetail} とHTTPステータスを返却する。
 */
@RestControllerAdvice(basePackages = "com.example.todoapp.todo")
@RequiredArgsConstructor
public class TodoExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(
        TodoExceptionHandler.class
    );

    private final ExceptionResponseBuilder exceptionResponseBuilder;

    /**
     * {@link TodoRequest} のフィールドの検証に失敗した場合の例外処理
     *
     * @param ex          {@link TodoFieldValidationException}
     * @param httpRequest HTTPリクエスト情報
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     */
    @ExceptionHandler(TodoFieldValidationException.class)
    public ResponseEntity<ProblemDetail> handleTodoFieldValidation(
        TodoFieldValidationException ex,
        HttpServletRequest httpRequest
    ) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorCode errorCode = ex.getErrorCode();

        logger.warn("[{}] {}", errorCode, ex.toString());

        return exceptionResponseBuilder.build(
            httpStatus,
            errorCode,
            ex.getErrorTitle(),
            httpRequest,
            ex.getField(),
            ex.getFieldValue()
        );
    }

    /**
     * To-doが存在しない場合の例外処理
     *
     * @param ex          {@link TodoEntityNotFoundException}
     * @param httpRequest HTTPリクエスト情報
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     */
    @ExceptionHandler(TodoEntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleTodoEntityNotFound(
        TodoEntityNotFoundException ex,
        HttpServletRequest httpRequest
    ) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ErrorCode errorCode = ex.getErrorCode();

        logger.warn("[{}] {}", errorCode, ex.toString());

        return exceptionResponseBuilder.build(
            httpStatus,
            errorCode,
            ex.getErrorTitle(),
            httpRequest
        );
    }
}
