package com.example.todoapp.etag.dto;

/**
 * ETagを含むレスポンスデータ転送オブジェクト
 *
 * @param <T>  レスポンスデータの型
 * @param data レスポンスデータ
 * @param etag ETagの値
 */
public record ResponseDtoWithETag<T>(
    T data,
    String etag
) {}
