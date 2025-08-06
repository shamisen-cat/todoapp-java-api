package com.example.todoapp.common.etag.factory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import org.springframework.stereotype.Component;

import com.example.todoapp.common.exception.ETagGenerationException;

/**
 * ETagを生成するジェネリクスクラス
 *
 * @param <T> ETagのベース文字列を提供するETagSourceを実装したクラス
 */
@Component
public class ETagGenerator<T extends ETagSource> {

    /**
     * ETagを生成する。
     *
     * @param source ETagのベース文字列を提供するインスタンス
     * @return 生成されたETagの文字列
     * @throws ETagGenerationException ETagの生成に失敗した場合
     */
    public String generate(T source) {
        if (source == null) {
            throw new ETagGenerationException(
                "ETagGenerator#generate",
                "Argument source is null."
            );
        }

        try {
            String base = source.getETagBase();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));

            return "\"" + Base64.getEncoder().encodeToString(hash) + "\"";

        } catch (Exception ex) {
            throw new ETagGenerationException(
                "ETagGenerator#generate",
                ex.getMessage() != null
                    ? ex.getMessage()
                    : ex.getClass().toString()
            );
        }
    }
}
