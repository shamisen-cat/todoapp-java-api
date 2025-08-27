package com.example.todoapp.common.error.handler.dto;

/**
 * リクエストパラメータの検証に関する例外から取り出したフィールド情報を保持する
 *
 * @param field      フィールド名
 * @param logMessage ログ出力用メッセージ
 */
public record FieldInfo(String field, String logMessage) {}
