package com.example.todoapp.todo.service.command;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.example.todoapp.etag.dto.ETagResponse;
import com.example.todoapp.etag.factory.ETagGenerator;
import com.example.todoapp.etag.validation.ETagValidator;
import com.example.todoapp.todo.dto.TodoRequest;
import com.example.todoapp.todo.dto.TodoResponse;
import com.example.todoapp.todo.model.TodoEntity;
import com.example.todoapp.todo.repository.TodoRepository;
import com.example.todoapp.todo.service.factory.TodoFactory;
import com.example.todoapp.todo.service.finder.TodoFinder;
import com.example.todoapp.todo.service.mapper.TodoMapper;

/**
 * Command操作のビジネスロジックを提供するサービスクラス
 */
@Service
@RequiredArgsConstructor
public class TodoCommandService {

    private final TodoFactory todoFactory;
    private final TodoFinder todoFinder;
    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;
    private final ETagGenerator<TodoEntity> eTagGenerator;
    private final ETagValidator eTagValidator;

    /**
     * To-doを作成し、{@link TodoResponse} を含む {@link ETagResponse} を返す。
     *
     * @param request 作成の {@link TodoRequest}
     * @return 作成結果の {@link TodoResponse} を含む {@link ETagResponse}
     */
    public ETagResponse<TodoResponse> createTodo(TodoRequest request) {
        TodoEntity created = todoFactory.createNew(request);
        TodoEntity saved = todoRepository.save(created);

        return new ETagResponse<TodoResponse>(
            todoMapper.toResponse(saved),
            eTagGenerator.generate(saved)
        );
    }

    /**
     * To-doを更新し、{@link TodoResponse} を含む {@link ETagResponse} を返す。
     *
     * @param id      更新対象のTo-doのID
     * @param request 更新の {@link TodoRequest}
     * @param ifMatch If-MatchヘッダのETag文字列値
     * @return 更新結果の {@link TodoResponse} を含む {@link ETagResponse}
     */
    public ETagResponse<TodoResponse> updateTodo(
        UUID id,
        TodoRequest request,
        String ifMatch
    ) {
        TodoEntity existing = todoFinder.getTodoByIdOrThrow(id);
        eTagValidator.assertETagEqualsExpected(
            ifMatch,
            eTagGenerator.generate(existing)
        );
        TodoEntity updated = todoFactory.applyUpdate(existing, request);
        TodoEntity saved = todoRepository.save(updated);

        return new ETagResponse<TodoResponse>(
            todoMapper.toResponse(saved),
            eTagGenerator.generate(saved)
        );
    }

    /**
     * 指定されたIDのTo-doを削除する。
     *
     * @param id      削除対象のTo-doのID
     * @param ifMatch If-MatchヘッダのETag文字列値
     */
    public void deleteTodo(UUID id, String ifMatch) {
        TodoEntity existing = todoFinder.getTodoByIdOrThrow(id);
        eTagValidator.assertETagEqualsExpected(
            ifMatch,
            eTagGenerator.generate(existing)
        );
        todoRepository.delete(existing);
    }
}
