package com.example.todoapp.etag.validation;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.example.todoapp.etag.exception.ETagMismatchException;
import com.example.todoapp.etag.exception.ETagMissingException;

/**
 * ETagに関するバリデータクラス
 */
@Component
public class ETagValidator {

    /**
     * ETagが {@code null} または {@code blank} ではないことを検証する。
     *
     * @param eTag 検証対象のETag文字列値
     * @throws ETagMissingException {@code eTag} が {@code null} または {@code blank} の場合
     */
    public void assertETagPresent(String eTag) {
        if (eTag == null || eTag.isBlank()) {
            throw new ETagMissingException(eTag);
        }
    }

    /**
     * ETagの整合性を検証する。
     *
     * @param eTag     検証対象のETagの文字列値
     * @param expected 比較対象のETagの文字列値
     * @throws ETagMismatchException {@code eTag} が {@code expected} と一致しない場合
     */
    public void assertETagEqualsExpected(String eTag, String expected) {
        if (!Objects.equals(eTag, expected)) {
            throw new ETagMismatchException(eTag, expected);
        }
    }
}
