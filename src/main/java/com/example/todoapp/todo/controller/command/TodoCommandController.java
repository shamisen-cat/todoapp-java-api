package com.example.todoapp.todo.controller.command;

import java.net.URI;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;

import com.example.todoapp.etag.dto.ETagResponse;
import com.example.todoapp.etag.validation.ETagValidator;
import com.example.todoapp.todo.dto.TodoRequest;
import com.example.todoapp.todo.dto.TodoResponse;
import com.example.todoapp.todo.service.command.TodoCommandService;

/**
 * Command操作のREST APIコントローラクラス
 */
@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Validated
public class TodoCommandController {

    private final TodoCommandService todoCommandService;
    private final ETagValidator eTagValidator;

    /**
     * To-doを作成する。
     *
     * @param request 作成の {@link TodoRequest}
     * @return 作成結果の {@link TodoResponse} を含むHTTP 201 Created {@link ResponseEntity}
     * @throws MethodArgumentNotValidException {@link TodoRequest} の検証に失敗した場合
     */
    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(
        @RequestBody
        @NotNull
        @Valid
        TodoRequest request
    ) {
        var result = todoCommandService.createTodo(request);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(result.data().id())
            .toUri();

        return ResponseEntity
            .created(location)
            .eTag(result.etag())
            .body(result.data());
    }

    /**
     * 指定されたIDのTo-doを更新する。
     *
     * @param id      更新対象のTo-doのID
     * @param request 更新の {@link TodoRequest}
     * @param ifMatch If-MatchヘッダのETag文字列値
     * @return 更新結果の {@link ETagResponse} を含む {@link ResponseEntity}
     * @throws MethodArgumentNotValidException {@link TodoRequest} の検証に失敗した場合
     */
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo(
        @PathVariable("id")
        UUID id,

        @RequestBody
        @NotNull
        @Valid
        TodoRequest request,

        @RequestHeader(value = "If-Match", required = false)
        String ifMatch
    ) {
        eTagValidator.assertETagPresent(ifMatch);
        var result = todoCommandService.updateTodo(id, request, ifMatch);

        return ResponseEntity
            .ok()
            .eTag(result.etag())
            .body(result.data());
    }

    /**
     * 指定されたIDのTo-doを削除する。
     *
     * @param id      削除対象のTo-doのID
     * @param ifMatch If-MatchヘッダのETag文字列値
     * @return HTTP 204 No Content {@link ResponseEntity}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(
        @PathVariable("id")
        UUID id,

        @RequestHeader(value = "If-Match", required = false)
        String ifMatch
    ) {
        eTagValidator.assertETagPresent(ifMatch);
        todoCommandService.deleteTodo(id, ifMatch);

        return ResponseEntity
            .noContent()
            .build();
    }
}
