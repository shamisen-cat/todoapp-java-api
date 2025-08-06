package com.example.todoapp.todo.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.todoapp.common.etag.dto.ResponseDtoWithETag;
import com.example.todoapp.common.etag.factory.ETagGenerator;
import com.example.todoapp.common.etag.validator.ETagValidator;
import com.example.todoapp.todo.dto.TodoRequestDto;
import com.example.todoapp.todo.dto.TodoResponseDto;
import com.example.todoapp.todo.dto.TodoResponseWithETagDto;
import com.example.todoapp.todo.exception.TodoEntityNotFoundException;
import com.example.todoapp.todo.factory.TodoFactory;
import com.example.todoapp.todo.mapper.TodoMapper;
import com.example.todoapp.todo.model.Todo;
import com.example.todoapp.todo.repository.TodoRepository;

/**
 * ToDoエンティティのビジネスロジックを担当するサービスクラス
 */
@Service
@Validated
public class TodoService {

    /**
     * ToDOエンティティのデータアクセスを扱うリポジトリインスタンス
     */
    private final TodoRepository todoRepository;

    /**
     * ToDoエンティティの生成・更新処理を提供するファクトリインスタンス
     */
    private final TodoFactory todoFactory;

    /**
     * ToDoエンティティとDTOの変換を行うマッパーインスタンス
     */
    private final TodoMapper todoMapper;

    /**
     * ToDoエンティティのETagを生成するユーティリティインスタンス
     */
    private final ETagGenerator<Todo> eTagGenerator;

    /**
     * ETagの検証を行うユーティリティインスタンス
     */
    private final ETagValidator eTagValidator;

    public TodoService(
        TodoRepository todoRepository,
        TodoFactory todoFactory,
        TodoMapper todoMapper,
        ETagGenerator<Todo> eTagGenerator,
        ETagValidator eTagValidator
    ) {
        this.todoRepository = todoRepository;
        this.todoFactory = todoFactory;
        this.todoMapper = todoMapper;
        this.eTagGenerator = eTagGenerator;
        this.eTagValidator = eTagValidator;
    }

    /**
     * ETagを含むToDoのレスポンスDTOのページングされたリストを取得する。
     *
     * @param page 0始まりのページ番号
     * @param size 1ページあたりの件数
     * @return ETagを含むToDoのレスポンスDTOのページングされたリストレスポンス
     */
    public Page<TodoResponseWithETagDto> getTodoPageByUpdatedAt(int page, int size) {
        Page<Todo> todoPage = todoRepository.findAll(
            PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "updatedAt")
            )
        );

        return todoPage.map(todo -> {
            String eTag = eTagGenerator.generate(todo);
            return todoMapper.toDto(todo, eTag);
        });
    }

    /**
     * 指定されたIDのToDoのレスポンスDTOと対応するETagを取得する。
     *
     * @param id 取得対象のToDoのID
     * @return 指定されたIDのToDoのレスポンスDTOと対応するETagを含むレスポンス
     */
    public ResponseDtoWithETag<TodoResponseDto> getTodo(UUID id) {
        Todo todo = findTodoByIdOrThrow(id);

        TodoResponseDto body = todoMapper.toDto(todo);
        String eTag = eTagGenerator.generate(todo);

        return new ResponseDtoWithETag<TodoResponseDto>(body, eTag);
    }

    /**
     * ToDoのリクエストDTOからToDoを新規作成し、レスポンスDTOと対応するETagを取得する。
     *
     * @param dto ToDoのリクエストDTO
     * @return 新規作成されたToDoのレスポンスDTOと対応するETagを含むレスポンス
     */
    public ResponseDtoWithETag<TodoResponseDto> createTodo(TodoRequestDto dto) {
        Todo todo = todoFactory.createNew(dto);
        Todo saved = todoRepository.save(todo);

        TodoResponseDto body = todoMapper.toDto(saved);
        String eTag = eTagGenerator.generate(saved);

        return new ResponseDtoWithETag<TodoResponseDto>(body, eTag);
    }

    /**
     * ToDoのリクエストDTOから既存のToDoを更新し、レスポンスDTOと対応するETagを取得する。
     *
     * @param id      更新対象のToDoのID
     * @param dto     ToDoのリクエストDTO
     * @param ifMatch クライアントから受信したIf-MatchヘッダのETag
     * @return 更新されたToDoのレスポンスDTOと対応するETagを含むレスポンス
     */
    public ResponseDtoWithETag<TodoResponseDto> updateTodo(
        UUID id,
        TodoRequestDto dto,
        String ifMatch
    ) {
        Todo existing = findTodoByIdOrThrow(id);
        String expected = eTagGenerator.generate(existing);
        eTagValidator.validate(ifMatch, expected);

        todoFactory.applyUpdate(existing, dto);
        Todo updated = todoRepository.save(existing);

        TodoResponseDto body = todoMapper.toDto(updated);
        String newETag = eTagGenerator.generate(updated);

        return new ResponseDtoWithETag<TodoResponseDto>(body, newETag);
    }

    /**
     * 指定されたIDのToDoを削除する。
     *
     * @param id      削除対象のToDoのID
     * @param ifMatch クライアントから受信したIf-MatchヘッダのETag
     */
    public void deleteTodo(UUID id, String ifMatch) {
        Todo todo = findTodoByIdOrThrow(id);
        String expected = eTagGenerator.generate(todo);
        eTagValidator.validate(ifMatch, expected);

        todoRepository.delete(todo);
    }

    /**
     * 指定されたIDのToDoを取得する。
     *
     * @param id 取得対象のToDoのID
     * @return 指定されたIDのToDoエンティティ
     */
    private Todo findTodoByIdOrThrow(UUID id) {
        return todoRepository
            .findById(id)
            .orElseThrow(() -> new TodoEntityNotFoundException(id));
    }
}
