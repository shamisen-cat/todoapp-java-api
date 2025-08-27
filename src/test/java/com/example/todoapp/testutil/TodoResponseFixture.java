package com.example.todoapp.testutil;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.todoapp.etag.dto.ETagResponse;
import com.example.todoapp.todo.dto.TodoResponse;

/**
 * テストで使用する {@link TodoResponse} を含む {@link ETagResponse} を生成するクラス。
 */
@Component
public class TodoResponseFixture {

    /**
     * {@link TodoResponse} を含む {@link ETagResponse} を生成する。
     *
     * @param id        ID
     * @param title     タイトル
     * @param completed 完了状態
     * @param createdAt 作成日時
     * @param updatedAt 更新日時
     * @param eTag      ETag文字列値
     * @return {@link TodoResponse} を含む {@link ETagResponse}
     */
    public ETagResponse<TodoResponse> create(
        UUID id,
        String title,
        boolean completed,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String eTag
    ) {
        return new ETagResponse<>(
            new TodoResponse(id, title, completed, createdAt, updatedAt),
            eTag
        );
    }
}
