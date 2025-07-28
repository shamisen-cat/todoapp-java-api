package com.example.todoapp.todo.dto;

/**
 * ToDoの作成・更新用データ転送オブジェクト
 *
 * @param title     タイトル
 * @param completed 完了状態（true: 完了、false: 未完了、null: 新規作成時）
 */
public record TodoRequestDto(
    String title,
    Boolean completed
) {}
