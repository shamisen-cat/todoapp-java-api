package com.example.todoapp.todo.service.factory;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.example.todoapp.todo.dto.TodoRequest;
import com.example.todoapp.todo.model.TodoEntity;
import com.example.todoapp.todo.service.factory.normalize.TitleNormalizer;

/**
 * {@link TodoEntity} を作成・更新するファクトリクラス
 */
@Component
@RequiredArgsConstructor
public class TodoFactory {

    private final TitleNormalizer titleNormalizer;

    /**
     * {@link TodoRequest} から {@link TodoEntity} を作成する。
     *
     * @param request {@link TodoEntity} 作成の {@link TodoRequest}
     * @return 作成結果の {@link TodoEntity}
     */
    public TodoEntity createNew (TodoRequest request) {
        String title = titleNormalizer.normalize(request.title());

        return new TodoEntity(title);
    }

    /**
     * {@link TodoRequest} から {@link TodoEntity} を更新する。
     *
     * @param existing 更新対象の {@link TodoEntity}
     * @param request  {@link TodoEntity} 更新の {@link TodoRequest}
     * @return 更新結果の {@link TodoEntity}
     */
    public TodoEntity applyUpdate(TodoEntity existing, TodoRequest request) {
        existing.setTitle(titleNormalizer.normalize(request.title()));
        existing.setCompleted(
            Optional
                .ofNullable(request.completed())
                .orElse(existing.isCompleted())
        );

        return existing;
    }
}
