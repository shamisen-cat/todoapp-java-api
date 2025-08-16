package com.example.todoapp.etag.service;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.example.todoapp.etag.exception.ETagMismatchException;
import com.example.todoapp.etag.exception.ETagMissingException;

/**
 * ETagを検証するバリデータ
 */
@Component
public class ETagValidator {

    /**
     * ETagの値が {@code null} または空文字ではないことを検証する。
     * @param eTag
     * @throws ETagMissingException {@code eTag} が {@code null} または空文字の場合
     */
    public void assertETagPresent(String eTag) {
        assertETagPresent(
            eTag,
            "ETagValidator#assertETagPresent"
        );
    }

    /**
     * ETagの値が {@code null} または空文字ではないことを検証する。
     *
     * @param eTag    検証対象のETagの値
     * @param context 例外に含めるコンテキストの識別情報
     * @throws ETagMissingException {@code eTag} が {@code null} または空文字の場合
     */
    public void assertETagPresent(String eTag, String context) {
        if (eTag == null || eTag.isBlank()) {
            throw new ETagMissingException(context, eTag);
        }
    }

    /**
     * ETagの整合性を検証する。
     *
     * @param eTag     検証対象のETagの値
     * @param expected 比較対象のETagの値
     * @throws ETagMismatchException {@code eTag} が {@code expected} と一致しない場合
     */
    public void assertETagEqualsExpected(String eTag, String expected) {
        assertETagEqualsExpected(
            eTag,
            expected,
            "ETagValidator#assertETagEqualsExpected"
        );
    }

    /**
     * ETagの整合性を検証する。
     *
     * @param eTag     検証対象のETagの値
     * @param expected 比較対象のETagの値
     * @param context  例外に含めるコンテキストの識別情報
     * @throws ETagMismatchException {@code eTag} が {@code expected} と一致しない場合
     */
    public void assertETagEqualsExpected(String eTag, String expected, String context) {
        if (!Objects.equals(eTag, expected)) {
            throw new ETagMismatchException(context, eTag, expected);
        }
    }
}
