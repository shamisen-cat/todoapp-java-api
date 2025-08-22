package com.example.todoapp.todo.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.todoapp.etag.dto.ResponseDtoWithETag;
import com.example.todoapp.etag.exception.ETagMismatchException;
import com.example.todoapp.etag.factory.ETagGenerator;
import com.example.todoapp.etag.service.ETagValidator;
import com.example.todoapp.todo.dto.TodoRequestDto;
import com.example.todoapp.todo.dto.TodoResponseDto;
import com.example.todoapp.todo.exception.TodoEntityNotFoundException;
import com.example.todoapp.todo.factory.TodoFactory;
import com.example.todoapp.todo.mapper.TodoMapper;
import com.example.todoapp.todo.model.Todo;
import com.example.todoapp.todo.repository.TodoRepository;

/**
 * CRUD操作のビジネスロジックを提供するサービス
 */
@Service
@Validated
public class TodoService {

    /** {@link Todo} を生成・更新するファクトリ */
    private final TodoFactory todoFactory;

    /** {@link Todo} のデータベースにアクセスするリポジトリ */
    private final TodoRepository todoRepository;

    /** {@link Todo} とDTOを変換するマッパー */
    private final TodoMapper todoMapper;

    /** ETagを生成するファクトリ */
    private final ETagGenerator<Todo> eTagGenerator;

    /** ETagを検証するバリデータ */
    private final ETagValidator eTagValidator;

    /**
     * CRUD操作のビジネスロジックを提供するサービスを生成する。
     *
     * @param todoFactory    {@link Todo} を生成・更新するファクトリ
     * @param todoRepository {@link Todo} のデータベースにアクセスするリポジトリ
     * @param todoMapper     {@link Todo} とDTOを変換するマッパー
     * @param eTagGenerator  ETagを生成するファクトリ
     * @param eTagValidator  ETagを検証するバリデータ
     */
    public TodoService(
        TodoFactory todoFactory,
        TodoRepository todoRepository,
        TodoMapper todoMapper,
        ETagGenerator<Todo> eTagGenerator,
        ETagValidator eTagValidator
    ) {
        this.todoFactory = todoFactory;
        this.todoRepository = todoRepository;
        this.todoMapper = todoMapper;
        this.eTagGenerator = eTagGenerator;
        this.eTagValidator = eTagValidator;
    }

    /**
     * {@link TodoResponseDto} を含む {@link ResponseDtoWithETag} の {@link Page} を取得する。
     *
     * @param page     0始まりのページ番号
     * @param pageSize 1ページあたりの件数
     * @return {@link TodoResponseDto} を含む {@link ResponseDtoWithETag} の {@link Page}
     */
    public Page<ResponseDtoWithETag<TodoResponseDto>> getTodoPageByUpdatedAt(
        int page,
        int pageSize
    ) {
        Page<Todo> todoPage = todoRepository.findAll(
            PageRequest.of(
                page,
                pageSize,
                Sort.by(Sort.Direction.DESC, "updatedAt")
            )
        );

        return todoPage.map(todo -> {
            TodoResponseDto dto = todoMapper.toResponseDto(todo);
            String eTag = eTagGenerator.generate(todo);

            return new ResponseDtoWithETag<TodoResponseDto>(dto, eTag);
        });
    }

    /**
     * 指定されたIDの {@link TodoResponseDto} を含む {@link ResponseDtoWithETag} を取得する。
     *
     * @param todoId 取得対象の {@link Todo} のID
     * @return 取得結果の {@link TodoResponseDto} を含む {@link ResponseDtoWithETag}
     * @throws TodoEntityNotFoundException 指定されたIDの {@link Todo} が存在しない場合
     */
    public ResponseDtoWithETag<TodoResponseDto> getTodo(UUID todoId) {
        Todo todo = getTodoByIdOrThrow(todoId, "TodoService#getTodo");

        TodoResponseDto body = todoMapper.toResponseDto(todo);
        String eTag = eTagGenerator.generate(todo);

        return new ResponseDtoWithETag<TodoResponseDto>(body, eTag);
    }

    /**
     * {@link Todo} を作成し、{@link TodoResponseDto} を含む {@link ResponseDtoWithETag} を返す。
     *
     * @param requestDto {@link Todo} 作成の {@link TodoRequestDto}
     * @return 作成結果の {@link TodoResponseDto} を含む {@link ResponseDtoWithETag}
     */
    public ResponseDtoWithETag<TodoResponseDto> createTodo(TodoRequestDto requestDto) {
        Todo created = todoFactory.createNew(requestDto);
        Todo saved = todoRepository.save(created);

        TodoResponseDto body = todoMapper.toResponseDto(saved);
        String eTag = eTagGenerator.generate(saved);

        return new ResponseDtoWithETag<TodoResponseDto>(body, eTag);
    }

    /**
     * {@link Todo} を更新し、{@link TodoResponseDto} を含む {@link ResponseDtoWithETag} を返す。
     *
     * @param todoId     更新対象の {@link Todo} のID
     * @param requestDto {@link Todo} 更新の {@link TodoRequestDto}
     * @param ifMatch    If-MatchヘッダのETagの値
     * @return 更新結果の {@link TodoResponseDto} を含む {@link ResponseDtoWithETag}
     * @throws TodoEntityNotFoundException 指定されたIDの {@link Todo} が存在しない場合
     * @throws ETagMismatchException       {@code ifMatch} の整合性検証に失敗した場合
     */
    public ResponseDtoWithETag<TodoResponseDto> updateTodo(
        UUID todoId,
        TodoRequestDto requestDto,
        String ifMatch
    ) {
        Todo existing = getTodoByIdOrThrow(todoId, "TodoService#updateTodo");
        eTagValidator.assertETagEqualsExpected(
            ifMatch,
            eTagGenerator.generate(existing),
            "TodoService#updateTodo"
        );

        Todo updated = todoFactory.applyUpdate(requestDto, existing);
        Todo saved = todoRepository.save(updated);

        TodoResponseDto body = todoMapper.toResponseDto(saved);
        String newETag = eTagGenerator.generate(saved);

        return new ResponseDtoWithETag<TodoResponseDto>(body, newETag);
    }

    /**
     * 指定されたIDの {@link Todo} を削除する。
     *
     * @param todoId  削除対象の {@link Todo} のID
     * @param ifMatch If-MatchヘッダのETagの値
     * @throws TodoEntityNotFoundException 指定されたIDの {@link Todo} が存在しない場合
     * @throws ETagMismatchException       {@code ifMatch} の整合性検証に失敗した場合
     */
    public void deleteTodo(UUID todoId, String ifMatch) {
        Todo existing = getTodoByIdOrThrow(todoId);
        eTagValidator.assertETagEqualsExpected(
            ifMatch,
            eTagGenerator.generate(existing),
            "TodoService#deleteTodo"
        );

        todoRepository.delete(existing);
    }

    /**
     * 指定されたIDの {@link Todo} を取得する。
     *
     * @param todoId 取得対象の {@link Todo} のID
     * @return 取得結果の {@link Todo}
     * @throws TodoEntityNotFoundException 指定されたIDの {@link Todo} が存在しない場合
     */
    private Todo getTodoByIdOrThrow(UUID todoId) {
        return getTodoByIdOrThrow(todoId, "TodoService#findTodoByIdOrThrow");
    }

    /**
     * 指定されたIDの {@link Todo} を取得する。
     *
     * @param todoId  取得対象の {@link Todo} のID
     * @param context 例外に含めるコンテキストの識別情報
     * @return 取得結果の {@link Todo}
     * @throws TodoEntityNotFoundException 指定されたIDの {@link Todo} が存在しない場合
     */
    private Todo getTodoByIdOrThrow(UUID todoId, String context) {
        return todoRepository
            .findById(todoId)
            .orElseThrow(() -> new TodoEntityNotFoundException(context, todoId));
    }
}
