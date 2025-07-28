package com.example.todoapp.todo.controller;

import java.net.URI;
import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.todoapp.common.dto.ResponseDtoWithETag;
import com.example.todoapp.todo.dto.TodoRequestDto;
import com.example.todoapp.todo.dto.TodoResponseDto;
import com.example.todoapp.todo.dto.TodoResponseWithETagDto;
import com.example.todoapp.todo.exception.InvalidTodoArgumentException;
import com.example.todoapp.todo.service.TodoService;

/**
 * ToDoのCRUD操作を提供するRESTコントローラクラス
 */
@RestController
@RequestMapping("/api/todos")
@Validated
public class TodoController {

    /**
     * ToDoエンティティのビジネスロジックを担当するサービスインスタンス
     */
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * ETagを含むToDoのページングされたリストを取得する。
     *
     * @param page 0始まりのページ番号
     * @param size 1ページあたりの件数
     * @return ETagを含むToDoのレスポンスDTOのページングされたリストレスポンス
     */
    @GetMapping
    public ResponseEntity<Page<TodoResponseWithETagDto>> getTodos(
        @RequestParam(value = "page", defaultValue = "0")
        @Min(0)
        int page,

        @RequestParam(value = "size", defaultValue = "10")
        @Min(1)
        @Max(100)
        int size
    ) {
        Page<TodoResponseWithETagDto> body = todoService.getTodoPageByUpdatedAt(page, size);

        return ResponseEntity.ok(body);
    }

    /**
     * 指定されたIDのToDoを取得する。
     *
     * @param id 取得対象のToDoのID
     * @return ToDoのレスポンスDTOと対応するETagを含むレスポンス
     */
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> getTodo(
        @PathVariable("id")
        UUID id
    ) {
        ResponseDtoWithETag<TodoResponseDto> result = todoService.getTodo(id);

        TodoResponseDto body = result.body();
        String eTag = result.eTag();

        return ResponseEntity
            .ok()
            .eTag(eTag)
            .body(body);
    }

    /**
     * ToDoを新規作成する。
     *
     * @param dto TodoのリクエストDTO
     * @return ToDoのレスポンスDTOとETag、HTTP 201 Createdを含むレスポンス
     */
    @PostMapping
    public ResponseEntity<TodoResponseDto> createTodo(
        @RequestBody
        @NotNull
        TodoRequestDto dto
    ) {
        ResponseDtoWithETag<TodoResponseDto> created = todoService.createTodo(dto);

        TodoResponseDto body = created.body();
        String eTag = created.eTag();

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(body.id())
            .toUri();

        return ResponseEntity
            .created(location)
            .eTag(eTag)
            .body(body);
    }

    /**
     * 指定されたIDのToDoを更新する。
     *
     * @param id         更新対象のToDoのID
     * @param requestDto 更新対象のToDoのリクエストDTO
     * @param ifMatch    クライアントから受信したIf-MatchヘッダのETag
     * @return ToDoのレスポンスDTOと対応するETagを含むレスポンス
     */
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDto> updateTodo(
        @PathVariable("id")
        UUID id,

        @RequestBody
        @NotNull
        TodoRequestDto dto,

        @RequestHeader(value = "If-Match", required = false)
        String ifMatch
    ) {
        validateIfMatch("TodoController#updateTodo", ifMatch);
        ResponseDtoWithETag<TodoResponseDto> updated = todoService.updateTodo(id, dto, ifMatch);

        TodoResponseDto body = updated.body();
        String eTag = updated.eTag();

        return ResponseEntity
            .ok()
            .eTag(eTag)
            .body(body);
    }

    /**
     * 指定されたIDのToDoを削除する。
     *
     * @param id      削除対象のToDoのID
     * @param ifMatch クライアントから受信したIf-MatchヘッダのETag
     * @return HTTP 204 No Contentを含むレスポンス
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(
        @PathVariable("id")
        UUID id,

        @RequestHeader(value = "If-Match", required = false)
        String ifMatch
    ) {
        validateIfMatch("TodoController#deleteTodo", ifMatch);
        todoService.deleteTodo(id, ifMatch);

        return ResponseEntity
            .noContent()
            .build();
    }

    /**
     * If-Matchヘッダの値がnullまたは空文字でないことを検証する。
     *
     * @param context メソッドのコンテキスト
     * @param ifMatch If-Matchヘッダの値
     * @throws InvalidTodoArgumentException If-Matchヘッダがnullまたは空文字の場合
     */
    private void validateIfMatch(String context, String ifMatch) {
        if (ifMatch == null || ifMatch.isBlank()) {
            throw new InvalidTodoArgumentException(
                context,
                "If-Match header",
                "Value was null or blank."
            );
        }
    }
}
