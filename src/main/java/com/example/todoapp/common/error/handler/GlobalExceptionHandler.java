package com.example.todoapp.common.error.handler;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.todoapp.common.error.ErrorCode;
import com.example.todoapp.common.error.factory.ProblemDetailFactory;
import com.example.todoapp.common.exception.ETagGenerationException;
import com.example.todoapp.common.exception.ETagMismatchException;
import com.example.todoapp.todo.exception.InvalidTodoArgumentException;
import com.example.todoapp.todo.exception.TodoEntityNotFoundException;

/**
 * REST APIにおける例外処理のグローバルハンドラクラス
 * <p>
 * 発生した例外に対して適切なHTTPステータスとエラーメッセージを返却する。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 想定外の例外をログ出力するロガーインスタンス
     */
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final ProblemDetailFactory problemDetailFactory;

    public GlobalExceptionHandler(ProblemDetailFactory problemDetailFactory) {
        this.problemDetailFactory = problemDetailFactory;
    }

    /**
     * バリデーションアノテーションで無効な引数と判断された場合の例外を処理する。
     * <p>
     * エラーログを出力し、HTTP 400 Bad Requestを返却する。
     *
     * @param ex 発生した例外
     * @return REST APIのエラー情報を含むレスポンス
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorCode errorCode = ErrorCode.VALIDATION_FAILURE;

        List<FieldError> fieldErrors = ex
            .getBindingResult()
            .getFieldErrors();

        FieldError firstError = fieldErrors.isEmpty()
            ? null
            : fieldErrors.get(0);

        String fieldName = (firstError != null)
            ? firstError.getField()
            : "UNKNOWN";
        String errorMessage = (firstError != null)
            ? firstError.getDefaultMessage()
            : "Validation failed.";

        ProblemDetail body = problemDetailFactory.create(
            status,
            errorCode,
            fieldName,
            errorMessage
        );

        logger.warn(
            "[{}] MethodArgumentNotValidException: {}",
            extractErrorCode(body),
            body.getDetail(),
            ex
        );

        return ResponseEntity
            .status(status)
            .body(body);
    }

    /**
     * ToDoに無効な引数が指定された場合の例外を処理する。
     * <p>
     * エラーログを出力し、HTTP 400 Bad Requestを返却する。
     *
     * @param ex 発生した例外
     * @return REST APIのエラー情報を含むレスポンス
     */
    @ExceptionHandler(InvalidTodoArgumentException.class)
    public ResponseEntity<ProblemDetail> handleInvalidTodoArgument(
        InvalidTodoArgumentException ex
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ProblemDetail body = problemDetailFactory.create(status, ex);

        logger.warn(
            "[{}] InvalidTodoArgumentException: {}",
            extractErrorCode(body),
            body.getDetail(),
            ex
        );

        return ResponseEntity
            .status(status)
            .body(body);
    }

    /**
     * ToDoが見つからなかった場合の例外を処理する。
     * <p>
     * HTTP 404 Not Foundを返却する。
     *
     * @param ex 発生した例外
     * @return REST APIのエラー情報を含むレスポンス
     */
    @ExceptionHandler(TodoEntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleTodoEntityNotFound(
        TodoEntityNotFoundException ex
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ProblemDetail body = problemDetailFactory.create(status, ex);

        return ResponseEntity
            .status(status)
            .body(body);
    }

    /**
     * ETagの検証に失敗した場合の例外を処理する。
     * <p>
     * エラーログを出力し、HTTP 412 Precondition Failedを返却する。
     *
     * @param ex 発生した例外
     * @return REST APIのエラー情報を含むレスポンス
     */
    @ExceptionHandler(ETagMismatchException.class)
    public ResponseEntity<ProblemDetail> handleETagMismatch(
        ETagMismatchException ex
    ) {
        HttpStatus status = HttpStatus.PRECONDITION_FAILED;

        ProblemDetail body = problemDetailFactory.create(status, ex);

        logger.warn(
            "[{}] ETagMismatchException: {}",
            extractErrorCode(body),
            body.getDetail(),
            ex
        );

        return ResponseEntity
            .status(status)
            .body(body);
    }

    /**
     * ETagの生成に失敗した場合の例外を処理する。
     * <p>
     * エラーログに出力し、HTTP 500 Internal Server Errorを返却する。
     *
     * @param ex 発生した例外
     * @return REST APIのエラー情報を含むレスポンス
     */
    @ExceptionHandler(ETagGenerationException.class)
    public ResponseEntity<ProblemDetail> handleETagGeneration(
        ETagGenerationException ex
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ProblemDetail body = problemDetailFactory.create(status, ex);

        logger.error(
            "[{}] ETagGenerationException: {}",
            extractErrorCode(body),
            body.getDetail(),
            ex
        );

        return ResponseEntity
            .status(status)
            .body(body);
    }

    /**
     * 設計上想定していない例外を処理する。
     * <p>
     * エラーログを出力し、HTTP 500 Internal Server Errorを返却する。
     *
     * @param ex 発生した例外
     * @return REST APIのエラー情報を含むレスポンス
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpectedException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        ProblemDetail body = problemDetailFactory.create(status, errorCode);

        logger.error(
            "[{}] Unexpected exception: {}",
            extractErrorCode(body),
            ex.getMessage(),
            ex
        );

        return ResponseEntity
            .status(status)
            .body(body);
    }

    /**
     * REST APIのエラー情報からエラーコードを取得する。
     *
     * @param detail REST APIのエラー情報
     * @return エラーコードの文字列識別子
     */
    private String extractErrorCode(ProblemDetail detail) {
        return Optional
            .ofNullable(detail.getProperties())
            .map(properties -> properties.get("errorCode"))
            .map(Object::toString)
            .orElse("UNKNOWN");
    }
}
