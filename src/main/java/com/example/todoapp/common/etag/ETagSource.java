package com.example.todoapp.common.etag;

/**
 * ETagを生成するベース文字列を提供するインタフェース
 */
public interface ETagSource {

    String getETagBase();
}
