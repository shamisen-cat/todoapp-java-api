package com.example.todoapp.todo.dto;

import jakarta.validation.constraints.NotNull;

import com.example.todoapp.common.validation.NotWhitespace;
import com.example.todoapp.todo.model.Todo;

/**
 * {@link Todo} の作成・更新で使用するリクエストデータ転送オブジェクト
 *
 * @param title     タイトル
 * @param completed 完了状態（true: 完了、false: 未完了、null: 新規作成または未選択時）
 */
public record TodoRequestDto(
    @NotNull(message = "{todo.title.notNull}")
    @NotWhitespace(message = "{todo.title.notWhitespace}")
    String title,

    Boolean completed
) {}
