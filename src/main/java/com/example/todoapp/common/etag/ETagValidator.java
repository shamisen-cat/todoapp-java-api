package com.example.todoapp.common.etag;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.example.todoapp.common.exception.ETagMismatchException;

/**
 * ETagの検証を行うユーティリティクラス
 */
@Component
public class ETagValidator {

    /**
     * クライアントから受信したIf-Matchヘッダを検証する。
     *
     * @param ifMatch  クライアントから受信したIf-Matchヘッダ
     * @param expected サーバ側のETag
     * @throws ETagMismatchException If-Matchヘッダが一致しなかった場合
     */
    public void validate(String ifMatch, String expected) {
        if (!Objects.equals(ifMatch, expected)) {
            throw new ETagMismatchException(
                "ETagValidator#validate",
                ifMatch,
                expected
            );
        }
    }
}
