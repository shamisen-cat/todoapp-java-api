package com.example.todoapp.todo.service.finder;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.example.todoapp.todo.exception.TodoEntityNotFoundException;
import com.example.todoapp.todo.model.TodoEntity;
import com.example.todoapp.todo.repository.TodoRepository;

/**
 * To-doの検索を行うクラス
 */
@Component
@RequiredArgsConstructor
public class TodoFinder {

    private final TodoRepository todoRepository;

    /**
     * 指定されたIDの {@link TodoEntity} を取得する。
     *
     * @param id 取得対象のTo-doのID
     * @return 取得結果の {@link TodoEntity}
     * @throws TodoEntityNotFoundException 指定されたIDのTo-doが存在しない場合
     */
    public TodoEntity getTodoByIdOrThrow(UUID id) {
        return todoRepository
            .findById(id)
            .orElseThrow(() -> new TodoEntityNotFoundException(id));
    }
}
