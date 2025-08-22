package com.example.todoapp.todo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.todoapp.todo.model.Todo;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * {@link Todo} のレスポンスデータ転送オブジェクト
 *
 * @param id        ID
 * @param title     タイトル
 * @param completed 完了状態（true: 完了、false: 未完了）
 * @param createdAt 作成日時
 * @param updatedAt 更新日時
 */
public record TodoResponseDto(
    UUID id,
    String title,
    boolean completed,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime updatedAt
) {}
