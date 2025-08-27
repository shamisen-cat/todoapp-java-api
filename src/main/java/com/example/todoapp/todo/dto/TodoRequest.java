package com.example.todoapp.todo.dto;

import jakarta.validation.constraints.NotNull;

import com.example.todoapp.common.validation.NotWhitespace;
import com.example.todoapp.todo.model.TodoEntity;

/**
 * {@link TodoEntity} の作成・更新で使用するリクエストデータ転送オブジェクト
 *
 * @param title     タイトル
 * @param completed 完了状態（true: 完了、false: 未完了、null: 作成または変更なし）
 */
public record TodoRequest(
    @NotNull(message = "{todo.title.notNull}")
    @NotWhitespace(message = "{todo.title.notWhitespace}")
    String title,

    Boolean completed
) {}
