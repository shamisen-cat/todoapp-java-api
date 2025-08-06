package com.example.todoapp.todo.dto;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ETagを含むクライアントに返すToDo情報のデータ転送オブジェクト
 *
 * @param id        ID
 * @param title     タイトル
 * @param completed 完了状態（true: 完了、false: 未完了）
 * @param createdAt 作成日時
 * @param updatedAt 更新日時
 * @param eTag      ETagの文字列
 */
public record TodoResponseWithETagDto(
    UUID id,
    String title,
    boolean completed,
    LocalDate createdAt,
    LocalDate updatedAt,

    @JsonProperty("etag")
    String eTag
) {}
