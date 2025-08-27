package com.example.todoapp.etag.factory;

/**
 * ETagのベース文字列値を取得するインタフェース
 */
public interface ETagSource {

    /**
     * ETagのベース文字列値を取得する。
     *
     * @return ETagのベース文字列値
     */
    String getETagBase();
}
