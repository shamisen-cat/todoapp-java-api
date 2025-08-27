package com.example.todoapp.etag.dto;

/**
 * ETagを含むレスポンスデータ転送オブジェクト
 *
 * @param <T>  レスポンスデータの型
 * @param data レスポンスデータ
 * @param etag ETag文字列値
 */
public record ETagResponse<T>(T data, String etag) {}
