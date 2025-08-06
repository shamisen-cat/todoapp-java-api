package com.example.todoapp.common.etag.dto;

/**
 * ETagによる整合性検証のためのレスポンスデータ転送オブジェクト
 *
 * @param body 検証対象のレスポンスDTO
 * @param eTag 検証対象に対応するETagの文字列
 */
public record ResponseDtoWithETag<T>(
    T body,
    String eTag
) {}
