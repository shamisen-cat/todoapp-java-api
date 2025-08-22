package com.example.todoapp.todo.factory;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.todoapp.todo.dto.TodoRequestDto;
import com.example.todoapp.todo.exception.TodoTitleValidationException;
import com.example.todoapp.todo.model.Todo;

/**
 * {@link Todo} を生成・更新するファクトリ
 */
@Component
public class TodoFactory {

    /**
     * {@link TodoRequestDto} から {@link Todo} を生成する。
     *
     * @param requestDto {@link Todo} 作成の {@link TodoRequestDto}
     * @return 作成結果の {@link Todo}
     */
    public Todo createNew (TodoRequestDto requestDto) {
        String title = normalizeTitle(requestDto.title());

        return new Todo(title);
    }

    /**
     * {@link TodoRequestDto} から {@link Todo} を更新する。
     *
     * @param requestDto {@link Todo} 更新の {@link TodoRequestDto}
     * @param existing   更新対象の {@link Todo}
     * @return 更新結果の {@link Todo}
     */
    public Todo applyUpdate(TodoRequestDto requestDto, Todo existing) {
        existing.setTitle(
            normalizeTitle(requestDto.title())
        );

        existing.setCompleted(
            Optional
                .ofNullable(requestDto.completed())
                .orElse(existing.isCompleted())
        );

        return existing;
    }

    /**
     * {@link Todo} のタイトルを正規化する。
     *
     * {@link Todo} の入力タイトル
     * @return {@link Todo} のタイトル
     */
    private String normalizeTitle(String title) {
        if (title == null) {
            throw new TodoTitleValidationException(
                "TodoFactory#normalizeTitle",
                title,
                "Title must not be null."
            );
        }

        String normalized = title.trim();

        if (normalized.isBlank()) {
            throw new TodoTitleValidationException(
                "TodoFactory#normalizeTitle",
                title,
                "Title must not be blank."
            );
        }

        if (normalized.length() > 100) {
            throw new TodoTitleValidationException(
                "TodoFactory#normalizeTitle",
                title,
                "Title must not exceed 100 characters."
            );
        }

        return normalized;
    }
}
