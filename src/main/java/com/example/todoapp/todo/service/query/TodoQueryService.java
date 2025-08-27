package com.example.todoapp.todo.service.query;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.example.todoapp.etag.dto.ETagResponse;
import com.example.todoapp.etag.factory.ETagGenerator;
import com.example.todoapp.todo.dto.TodoResponse;
import com.example.todoapp.todo.model.TodoEntity;
import com.example.todoapp.todo.repository.TodoRepository;
import com.example.todoapp.todo.service.finder.TodoFinder;
import com.example.todoapp.todo.service.mapper.TodoMapper;

/**
 * Query操作のビジネスロジックを提供するサービスクラス
 */
@Service
@RequiredArgsConstructor
public class TodoQueryService {

    private final TodoFinder todoFinder;
    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;
    private final ETagGenerator<TodoEntity> eTagGenerator;

    /**
     * 指定されたIDの {@link TodoResponse} を含む {@link ETagResponse} を取得する。
     *
     * @param id 取得対象のTo-doのID
     * @return 取得結果の {@link TodoResponse} を含む {@link ETagResponse}
     */
    public ETagResponse<TodoResponse> getTodo(UUID id) {
        TodoEntity todo = todoFinder.getTodoByIdOrThrow(id);

        return new ETagResponse<TodoResponse>(
            todoMapper.toResponse(todo),
            eTagGenerator.generate(todo)
        );
    }

    /**
     * {@link TodoResponse} の {@link Page} を取得する。
     *
     * @param page ページ番号
     * @param size 表示件数
     * @return 取得結果の {@link TodoResponse} の {@link Page}
     */
    public Page<ETagResponse<TodoResponse>> getTodoPage(int page, int size) {
        Page<TodoEntity> todoPage = todoRepository.findAll(
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"))
        );

        return todoPage.map(todo -> {
            TodoResponse response = todoMapper.toResponse(todo);
            String eTag = eTagGenerator.generate(todo);

            return new ETagResponse<TodoResponse>(response, eTag);
        });
    }
}
