package com.example.todoapp.etag.factory;

/**
 * ETagのベース文字列を取得するためのインタフェース
 */
public interface ETagSource {

    /**
     * ETagのベース文字列を取得する。
     *
     * @return ETagのベース文字列
     */
    String getETagBase();
}
