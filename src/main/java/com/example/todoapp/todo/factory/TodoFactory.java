package com.example.todoapp.todo.factory;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.todoapp.config.TodoProperties;
import com.example.todoapp.todo.dto.TodoRequestDto;
import com.example.todoapp.todo.exception.InvalidTodoArgumentException;
import com.example.todoapp.todo.model.Todo;

/**
 * ToDoエンティティの生成・更新処理を提供するファクトリクラス
 */
@Component
public class TodoFactory {

    /**
     * アプリケーションの設定値を保持するプロパティインスタンス
     */
    private final TodoProperties todoProperties;

    public TodoFactory(TodoProperties todoProperties) {
        this.todoProperties = todoProperties;
    }

    /**
     * ToDoリクエストDTOから新規のToDoエンティティを生成する。
     *
     * @param dto ToDoリクエストDTO
     * @return 新規のToDoエンティティ
     */
    public Todo createNew (TodoRequestDto dto) {
        String title = normalizeTitle(dto.title());

        return new Todo(title);
    }

    /**
     * ToDoリクエストDTOからToDoエンティティを更新する。
     *
     * @param existing 更新対象のTodoエンティティ
     * @param dto      ToDoリクエストDTO
     * @return 更新後のToDoエンティティ
     */
    public Todo applyUpdate(Todo existing, TodoRequestDto dto) {
        String title = normalizeTitle(dto.title());
        boolean completed = Optional
            .ofNullable(dto.completed())
            .orElse(existing.isCompleted());
        existing.setTitle(title);
        existing.setCompleted(completed);

        return existing;
    }

    /**
     * ToDoのタイトルがnullまたは空文字の場合にデフォルトタイトルに置き換える。
     *
     * @param title ToDoの入力タイトル
     * @return ToDoの入力タイトルまたはデフォルトタイトル
     * @throws InvalidTodoArgumentException ToDoのタイトルが100文字を超えている場合
     */
    private String normalizeTitle(String title) {
        String normalized = Optional
            .ofNullable(title)
            .filter(t -> !t.isBlank())
            .orElse(todoProperties.getDefaultTitle());

        if (normalized.length() > 100) {
            throw new InvalidTodoArgumentException(
                "TodoFactory#normalizeTitle",
                "title",
                "Length exceeded 100 characters."
            );
        }

        return normalized;
    }
}
