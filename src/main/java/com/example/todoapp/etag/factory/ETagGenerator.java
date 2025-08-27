package com.example.todoapp.etag.factory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.stereotype.Component;

import com.example.todoapp.etag.exception.ETagGenerationException;

/**
 * ETagを生成するファクトリクラス
 *
 * @param <T> ETagのベース文字列値を取得する {@link ETagSource} を実装したクラス
 */
@Component
public class ETagGenerator<T extends ETagSource> {

    /**
     * ETagを生成する。
     *
     * @param source  {@link ETagSource} を実装したインスタンス
     * @return ETag文字列値
     * @throws ETagGenerationException ETagの生成に失敗した場合
     */
    public String generate(T source) {
        if (source == null) {
            throw new ETagGenerationException("Argument 'source' is null.");
        }

        try {
            String base = source.getETagBase();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));

            return String.format("\"%s\"", Base64.getEncoder().encodeToString(hash));

        } catch (NoSuchAlgorithmException e) {
            throw new ETagGenerationException(e.getMessage(), e);
        }
    }
}
