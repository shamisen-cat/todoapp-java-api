package com.example.todoapp.common.error;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.todoapp.common.exception.InvalidValidationException;
import com.example.todoapp.etag.exception.ETagGenerationException;
import com.example.todoapp.etag.exception.ETagMismatchException;
import com.example.todoapp.todo.exception.TodoTitleValidationException;
import com.example.todoapp.todo.model.Todo;
import com.example.todoapp.todo.exception.TodoEntityNotFoundException;

/**
 * REST APIのグローバルエラーハンドラ
 * <p>
 * クライアントに {@link ProblemDetail} とHTTPステータスを返却する。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** リクエストパラメータの検証に失敗した場合のエラーメッセージ */
    private static final String REQUEST_VALIDATION_ERROR_MESSAGE =
        "Request Validation Failure";

    /** 想定外の例外が発生した場合のエラーメッセージ */
    private static final String INTERNAL_SERVER_ERROR_MESSAGE =
        "Internal Server Error";

    /** エラー出力用ロガー */
    private static final Logger logger =
        LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** REST APIで返却する {@link ProblemDetail} を生成するファクトリ */
    private final ProblemDetailFactory problemDetailFactory;

    /**
     * REST APIのグローバルエラーハンドラを生成する。
     *
     * @param problemDetailFactory REST APIで返却する {@link ProblemDetail} を生成するファクトリ
     */
    public GlobalExceptionHandler(ProblemDetailFactory problemDetailFactory) {
        this.problemDetailFactory = problemDetailFactory;
    }

    /**
     * メソッド引数のバリデーション {@code @Valid} に失敗した場合の例外処理
     * <p>
     * エラーログを出力し、HTTP 400 Bad Request を返却する。
     *
     * @param ex {@link MethodArgumentNotValidException}
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     * @throws InvalidValidationException 例外処理で異常が発生した場合
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorCode errorCode = ErrorCode.REQUEST_VALIDATION_FAILURE;

        FieldError firstError = ex
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .findFirst()
            .orElseThrow(() -> new InvalidValidationException(
                "GlobalExceptionHandler#handleMethodArgumentNotValid",
                "FieldError is empty."
            ));

        String field = Optional
            .ofNullable(firstError.getField())
            .filter(f -> !f.isBlank())
            .orElseThrow(() -> new InvalidValidationException(
                "GlobalExceptionHandler#handleMethodArgumentNotValid",
                "FieldError 'field' is empty."
            ));

        String message = Optional
            .ofNullable(firstError.getDefaultMessage())
            .filter(m -> !m.isBlank())
            .orElseThrow(() -> new InvalidValidationException(
                "GlobalExceptionHandler#handleMethodArgumentNotValid",
                "FieldError 'defaultMessage' is empty."
            ));

        ProblemDetail body = problemDetailFactory.create(
            status,
            errorCode,
            REQUEST_VALIDATION_ERROR_MESSAGE,
            request,
            field
        );

        logger.warn(
            "[{}] {}: {} [field={}, message={}]",
            errorCode,
            ex.getClass().getSimpleName(),
            REQUEST_VALIDATION_ERROR_MESSAGE,
            field,
            message,
            ex
        );

        return ResponseEntity
            .status(status)
            .body(body);
    }

    /**
     * メソッド引数のバリデーション {@code @Validated} に失敗した場合の例外処理
     * <p>
     * エラーログを出力し、HTTP 400 Bad Request を返却する。
     *
     * @param ex {@link HandlerMethodValidationException}
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     * @throws InvalidValidationException 例外処理で異常が発生した場合
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ProblemDetail> handleHandlerMethodValidation(
        HandlerMethodValidationException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorCode errorCode = ErrorCode.REQUEST_VALIDATION_FAILURE;

        ParameterValidationResult firstResult = ex
            .getValueResults()
            .stream()
            .findFirst()
            .orElseThrow(() -> new InvalidValidationException(
                "GlobalExceptionHandler#handleHandlerMethodValidation",
                "ParameterValidationResult is empty."
            ));

        String field = Optional
            .ofNullable(firstResult.getMethodParameter().getParameterName())
            .filter(n -> !n.isBlank())
            .orElseThrow(() -> new InvalidValidationException(
                "GlobalExceptionHandler#handleHandlerMethodValidation",
                "ParameterValidationResult 'parameterName' is empty."
            ));

        String message = firstResult
            .getResolvableErrors()
            .stream()
            .map(MessageSourceResolvable::getDefaultMessage)
            .findFirst()
            .filter(m -> !m.isBlank())
            .orElseThrow(() -> new InvalidValidationException(
                "GlobalExceptionHandler#handleHandlerMethodValidation",
                "ParameterValidationResult 'defaultMessage' is empty."
            ));

        ProblemDetail body = problemDetailFactory.create(
            status,
            errorCode,
            REQUEST_VALIDATION_ERROR_MESSAGE,
            request,
            field
        );

        logger.warn(
            "[{}] {}: {} [field={}, message={}]",
            errorCode,
            ex.getClass().getSimpleName(),
            REQUEST_VALIDATION_ERROR_MESSAGE,
            field,
            message,
            ex
        );

        return ResponseEntity
            .status(status)
            .body(body);
    }

    /**
     * Bean Validation の制約違反でバリデーションに失敗した場合の例外処理
     * <p>
     * エラーログを出力し、HTTP 400 Bad Request を返却する。
     *
     * @param ex {@link ConstraintViolationException}
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     * @throws InvalidValidationException 例外処理で異常が発生した場合
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(
        ConstraintViolationException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorCode errorCode = ErrorCode.REQUEST_VALIDATION_FAILURE;

        ConstraintViolation<?> firstViolation = ex
            .getConstraintViolations()
            .stream()
            .findFirst()
            .orElseThrow(() -> new InvalidValidationException(
                "GlobalExceptionHandler#handleConstraintViolation",
                "ConstraintViolation is empty."
            ));

        String field = Optional
            .ofNullable(firstViolation.getPropertyPath().toString())
            .filter(p -> !p.isBlank())
            .orElseThrow(() -> new InvalidValidationException(
                "GlobalExceptionHandler#handleConstraintViolation",
                "ConstraintViolation propertyPath is empty."
            ));

        String message = Optional
            .ofNullable(firstViolation.getMessage())
            .filter(m -> !m.isBlank())
            .orElseThrow(() -> new InvalidValidationException(
                "GlobalExceptionHandler#handleConstraintViolation",
                "ConstraintViolation message is empty."
            ));

        ProblemDetail body = problemDetailFactory.create(
            status,
            errorCode,
            REQUEST_VALIDATION_ERROR_MESSAGE,
            request,
            field
        );

        logger.warn(
            "[{}] {}: {} [field={}, message={}]",
            errorCode,
            ex.getClass().getSimpleName(),
            REQUEST_VALIDATION_ERROR_MESSAGE,
            field,
            message,
            ex
        );

        return ResponseEntity
            .status(status)
            .body(body);
    }

    /**
     * リクエストパラメータの型変換に失敗した場合の例外処理
     * <p>
     * エラーログを出力し、HTTP 400 Bad Request を返却する。
     *
     * @param ex {@link MethodArgumentTypeMismatchException}
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     * @throws InvalidValidationException 例外処理で異常が発生した場合
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatch(
        MethodArgumentTypeMismatchException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorCode errorCode = ErrorCode.REQUEST_VALIDATION_FAILURE;

        String field = Optional
            .ofNullable(ex.getName())
            .filter(n -> !n.isBlank())
            .orElseThrow(() -> new InvalidValidationException(
                "GlobalExceptionHandler#handleMethodArgumentTypeMismatch",
                "MethodArgumentTypeMismatch name is empty."
            ));

        String message = Optional
            .ofNullable(ex.getMessage())
            .filter(m -> !m.isBlank())
            .orElseThrow(() -> new InvalidValidationException(
                "GlobalExceptionHandler#handleMethodArgumentTypeMismatch",
                "MethodArgumentTypeMismatch message is empty."
            ));

        ProblemDetail body = problemDetailFactory.create(
            status,
            errorCode,
            REQUEST_VALIDATION_ERROR_MESSAGE,
            request,
            field
        );

        logger.warn(
            "[{}] {}: {} [field={}, message={}]",
            errorCode,
            ex.getClass().getSimpleName(),
            REQUEST_VALIDATION_ERROR_MESSAGE,
            field,
            message,
            ex
        );

        return ResponseEntity
            .status(status)
            .body(body);
    }

    /**
     * {@link Todo} のタイトルの検証に失敗した場合の例外処理
     * <p>
     * エラーログを出力し、HTTP 400 Bad Request を返却する。
     *
     * @param ex {@link TodoTitleValidationException}
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     */
    @ExceptionHandler(TodoTitleValidationException.class)
    public ResponseEntity<ProblemDetail> handleTodoTitleValidation(
        TodoTitleValidationException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ProblemDetail body = problemDetailFactory.create(
            status,
            ex,
            request,
            ex.getReason()
        );

        logger.warn(
            "[{}] {}: {}",
            ex.getErrorCode(),
            ex.getClass().getSimpleName(),
            ex.getMessage(),
            ex
        );

        return ResponseEntity
            .status(status)
            .body(body);
    }

    /**
     * {@link Todo} が存在しない場合の例外処理
     * <p>
     * エラーログを出力し、HTTP 404 Not Found を返却する。
     *
     * @param ex {@link TodoEntityNotFoundException}
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     */
    @ExceptionHandler(TodoEntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleTodoEntityNotFound(
        TodoEntityNotFoundException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ProblemDetail body = problemDetailFactory.create(status, ex, request);

        logger.warn(
            "[{}] {}: {}",
            ex.getErrorCode(),
            ex.getClass().getSimpleName(),
            ex.getMessage(),
            ex
        );

        return ResponseEntity
            .status(status)
            .body(body);
    }

    /**
     * ETagの整合性検証に失敗した場合の例外処理
     * <p>
     * エラーログを出力し、HTTP 412 Precondition Failed を返却する。
     *
     * @param ex {@link ETagMismatchException}
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     */
    @ExceptionHandler(ETagMismatchException.class)
    public ResponseEntity<ProblemDetail> handleETagMismatch(
        ETagMismatchException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.PRECONDITION_FAILED;
        ErrorCode errorCode = ErrorCode.ETAG_MISMATCH;

        ProblemDetail body = problemDetailFactory.create(
            status,
            errorCode,
            ex,
            request
        );

        logger.warn(
            "[{}] {}: {}",
            errorCode,
            ex.getClass().getSimpleName(),
            ex.getMessage(),
            ex
        );

        return ResponseEntity
            .status(status)
            .body(body);
    }

    /**
     * ETagの生成に失敗した場合の例外処理
     * <p>
     * エラーログに出力し、HTTP 500 Internal Server Error を返却する。
     *
     * @param ex {@link ETagGenerationException}
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     */
    @ExceptionHandler(ETagGenerationException.class)
    public ResponseEntity<ProblemDetail> handleETagGeneration(
        ETagGenerationException ex,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorCode errorCode = ErrorCode.ETAG_GENERATION_FAILURE;

        ProblemDetail body = problemDetailFactory.create(
            status,
            errorCode,
            ex,
            request
        );

        logger.error(
            "[{}] {}: {}",
            errorCode,
            ex.getClass().getSimpleName(),
            ex.getMessage(),
            ex
        );

        return ResponseEntity
            .status(status)
            .body(body);
    }

    /**
     * 想定外の例外処理
     * <p>
     * エラーログを出力し、HTTP 500 Internal Server Error を返却する。
     *
     * @param ex 想定外の例外
     * @return {@link ProblemDetail} を含む {@link ResponseEntity}
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpectedException(
        Exception ex,
        HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        ProblemDetail body = problemDetailFactory.create(
            status,
            errorCode,
            INTERNAL_SERVER_ERROR_MESSAGE,
            request
        );

        logger.error(
            "[{}] {}: {}",
            errorCode,
            ex.getClass().getSimpleName(),
            INTERNAL_SERVER_ERROR_MESSAGE,
            ex
        );

        return ResponseEntity
            .status(status)
            .body(body);
    }
}
