package com.example.todoapp.todo.controller.query;

import java.util.UUID;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.RequiredArgsConstructor;

import com.example.todoapp.etag.dto.ETagResponse;
import com.example.todoapp.todo.dto.TodoResponse;
import com.example.todoapp.todo.service.query.TodoQueryService;

/**
 * Query操作のREST APIコントローラクラス
 */
@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Validated
public class TodoQueryController {

    private final TodoQueryService todoQueryService;

    /**
     * 指定されたIDのTo-doを取得する。
     *
     * @param id 取得対象のTo-doのID
     * @return 取得結果の{@link TodoResponse} を含む {@link ResponseEntity}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> getTodo(
        @PathVariable("id")
        UUID id
    ) {
        var result = todoQueryService.getTodo(id);

        return ResponseEntity
            .ok()
            .eTag(result.etag())
            .body(result.data());
    }

    /**
     * {@link TodoResponse} の {@link Page} を取得する。
     *
     * @param page ページ番号
     * @param size 表示件数
     * @return 取得結果の {@link TodoResponse} の {@link Page} を含む {@link ResponseEntity}
     * @throws ConstraintViolationException        クエリパラメータの検証に失敗した場合
     * @throws MethodArgumentTypeMismatchException クエリパラメータが数値ではない場合
     */
    @GetMapping
    public ResponseEntity<Page<ETagResponse<TodoResponse>>> getTodos(
        @RequestParam(value = "page", defaultValue = "0")
        @Min(0)
        int page,

        @RequestParam(value = "size", defaultValue = "10")
        @Min(1)
        @Max(100)
        int size
    ) {
        var body = todoQueryService.getTodoPage(page, size);

        return ResponseEntity.ok(body);
    }
}
