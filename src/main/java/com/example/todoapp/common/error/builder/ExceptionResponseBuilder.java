package com.example.todoapp.common.error.builder;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.example.todoapp.common.error.ErrorCode;
import com.example.todoapp.common.error.builder.factory.ProblemDetailFactory;

/**
 * {@link ProblemDetail} を含む {@link ResponseEntity} を構築するためのビルダクラス
 */
@Component
@RequiredArgsConstructor
public class ExceptionResponseBuilder {

    private final ProblemDetailFactory problemDetailFactory;

    /**
     * {@link ProblemDetail} を含む {@link ResponseEntity} を構築する。
     *
     * @param httpStatus  HTTPステータス
     * @param errorCode   エラーコード
     * @param errorTitle  エラータイトル
     * @param httpRequest HTTPリクエスト情報
     * @param messageArgs {@link ErrorCode#messageTemplate} の引数
     * @return
     */
    public ResponseEntity<ProblemDetail> build(
        HttpStatus httpStatus,
        ErrorCode errorCode,
        String errorTitle,
        HttpServletRequest httpRequest,
        Object... messageArgs
    ) {
        ProblemDetail body = problemDetailFactory.create(
            httpStatus,
            errorCode,
            errorTitle,
            httpRequest,
            messageArgs
        );

        return ResponseEntity.status(httpStatus).body(body);
    }
}
