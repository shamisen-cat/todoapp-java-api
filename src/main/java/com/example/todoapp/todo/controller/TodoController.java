package com.example.todoapp.todo.controller;

import java.net.URI;
import java.util.UUID;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.todoapp.etag.dto.ResponseDtoWithETag;
import com.example.todoapp.etag.service.ETagValidator;
import com.example.todoapp.todo.dto.TodoRequestDto;
import com.example.todoapp.todo.dto.TodoResponseDto;
import com.example.todoapp.todo.model.Todo;
import com.example.todoapp.todo.service.TodoService;

/**
 * CRUD操作のREST APIコントローラ
 * <p>
 * クライアントにCRUD操作の結果とHTTPステータスを返却する。
 */
@RestController
@RequestMapping("/api/todos")
@Validated
public class TodoController {

    /** CRUD操作のビジネスロジックを提供するサービス */
    private final TodoService todoService;

    /** ETagを検証するバリデータ */
    private final ETagValidator eTagValidator;

    /**
     * CRUD操作のREST APIコントローラを生成する。
     *
     * @param todoService   CRUD操作のビジネスロジックを提供するサービス
     * @param eTagValidator ETagを検証するバリデータ
     */
    public TodoController(
        TodoService todoService,
        ETagValidator eTagValidator
    ) {
        this.todoService = todoService;
        this.eTagValidator = eTagValidator;
    }

    /**
     * {@link TodoResponseDto} を含む {@link Page} を取得する。
     *
     * @param page     0始まりのページ番号
     * @param pageSize 1ページあたりの件数
     * @return {@link ResponseDtoWithETag} の {@link Page} を含む {@link ResponseEntity}
     * @throws ConstraintViolationException        {@code page} または {@code pageSize} の検証に失敗した場合
     * @throws MethodArgumentTypeMismatchException {@code page} または {@code pageSize} が数値ではない場合
     */
    @GetMapping
    public ResponseEntity<Page<ResponseDtoWithETag<TodoResponseDto>>> getTodos(
        @RequestParam(value = "page", defaultValue = "0")
        @Min(0)
        int page,

        @RequestParam(value = "size", defaultValue = "10")
        @Min(1)
        @Max(100)
        int pageSize
    ) {
        var body = todoService.getTodoPageByUpdatedAt(page, pageSize);

        return ResponseEntity.ok(body);
    }

    /**
     * 指定されたIDの {@link TodoResponseDto} を取得する。
     *
     * @param todoId 取得対象の {@link Todo} のID
     * @return 取得結果の {@link ResponseDtoWithETag} を含む {@link ResponseEntity}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> getTodo(
        @PathVariable("id")
        UUID todoId
    ) {
        var result = todoService.getTodo(todoId);

        return ResponseEntity.ok()
            .eTag(result.etag())
            .body(result.data());
    }

    /**
     * {@link Todo} を作成する。
     *
     * @param requestDto {@link Todo} 作成の {@link TodoRequestDto}
     * @return 作成結果の {@link ResponseDtoWithETag} を含む HTTP 201 Created {@link ResponseEntity}
     * @throws MethodArgumentNotValidException {@code requestDto} の検証に失敗した場合
     */
    @PostMapping
    public ResponseEntity<TodoResponseDto> createTodo(
        @RequestBody
        @NotNull // メッセージコンバータ例外のため、null テストは不要
        @Valid
        TodoRequestDto requestDto
    ) {
        var result = todoService.createTodo(requestDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(result.data().id())
            .toUri();

        return ResponseEntity
            .created(location)
            .eTag(result.etag())
            .body(result.data());
    }

    /**
     * 指定されたIDの {@link Todo} を更新する。
     *
     * @param todoId     更新対象の {@link Todo} のID
     * @param requestDto {@link Todo} 更新の {@link TodoRequestDto}
     * @param ifMatch    If-MatchヘッダのETagの値
     * @return 更新結果の {@link ResponseDtoWithETag} を含む {@link ResponseEntity}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDto> updateTodo(
        @PathVariable("id")
        UUID todoId,

        @RequestBody
        @NotNull // メッセージコンバータ例外のため、null テストは不要
        @Valid
        TodoRequestDto requestDto,

        @RequestHeader(value = "If-Match", required = false)
        String ifMatch
    ) {
        eTagValidator.assertETagPresent(ifMatch, "TodoController#updateTodo");
        var result = todoService.updateTodo(todoId, requestDto, ifMatch);

        return ResponseEntity.ok()
            .eTag(result.etag())
            .body(result.data());
    }

    /**
     * 指定されたIDの {@link Todo} を削除する。
     *
     * @param todoId  削除対象の {@link Todo} のID
     * @param ifMatch If-MatchヘッダのETagの値
     * @return HTTP 204 No Content {@link ResponseEntity}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(
        @PathVariable("id")
        UUID todoId,

        @RequestHeader(value = "If-Match", required = false)
        String ifMatch
    ) {
        eTagValidator.assertETagPresent(ifMatch, "TodoController#deleteTodo");
        todoService.deleteTodo(todoId, ifMatch);

        return ResponseEntity.noContent().build();
    }
}
