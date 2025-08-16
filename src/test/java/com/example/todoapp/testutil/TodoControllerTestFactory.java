package com.example.todoapp.testutil;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.example.todoapp.etag.dto.ResponseDtoWithETag;
import com.example.todoapp.todo.controller.TodoController;
import com.example.todoapp.todo.dto.TodoRequestDto;
import com.example.todoapp.todo.dto.TodoResponseDto;
import com.example.todoapp.todo.model.Todo;

import static com.example.todoapp.testutil.TestConstants.*;

/**
 * {@link TodoController} のテストデータを生成するファクトリ
 */
@Component
public class TodoControllerTestFactory {

    /**
     * {@link TodoController#getTodos} テストの {@link Page} を生成する。
     *
     * @param page 0始まりのページ番号
     * @param size 1ページあたりの件数
     * @return {@link ResponseDtoWithETag} の {@link Page}
     */
    public Page<ResponseDtoWithETag<TodoResponseDto>> createPageResponse(
        int page,
        int size
    ) {
        List<ResponseDtoWithETag<TodoResponseDto>> todos = List.of(
            new ResponseDtoWithETag<>(
                new TodoResponseDto(
                    TODO_ID_1,
                    VALID_TITLE_1,
                    true,
                    CREATED_DATE_1,
                    UPDATED_DATE_1
                ),
                E_TAG_1
            ),

            new ResponseDtoWithETag<>(
                new TodoResponseDto(
                    TODO_ID_2,
                    VALID_TITLE_2,
                    false,
                    CREATED_DATE_2,
                    UPDATED_DATE_2
                ),
                E_TAG_2
            )
        );
        Pageable pageable = PageRequest.of(page, size);

        return new PageImpl<>(todos, pageable, todos.size());
    }

    /**
     * {@link TodoController#getTodo} テストの {@link ResponseDtoWithETag} を生成する。
     *
     * @param todoId 取得対象の {@link Todo} のID
     * @return {@link TodoResponseDto} の {@link ResponseDtoWithETag}
     */
    public ResponseDtoWithETag<TodoResponseDto> createResponseFromId(UUID todoId) {
        TodoResponseDto todo = new TodoResponseDto(
            todoId,
            VALID_TITLE_1,
            false,
            CREATED_DATE_1,
            UPDATED_DATE_1
        );

        return new ResponseDtoWithETag<>(todo, E_TAG_1);
    }

    /**
     * {@link TodoController#createTodo} テストの {@link ResponseDtoWithETag} を生成する。
     *
     * @param requestDto {@link TodoRequestDto}
     * @return {@link TodoResponseDto} の {@link ResponseDtoWithETag}
     */
    public ResponseDtoWithETag<TodoResponseDto> createResponseFromCreateRequest(
        TodoRequestDto requestDto
    ) {
        TodoResponseDto todo = new TodoResponseDto(
            TODO_ID_1,
            requestDto.title(),
            false,
            CREATED_DATE_1,
            CREATED_DATE_1
        );

        return new ResponseDtoWithETag<>(todo, E_TAG_1);
    }

    /**
     * {@link TodoController#updateTodo} テストの {@link ResponseDtoWithETag} を生成する。
     *
     * @param todoId     更新対象の {@link Todo} のID
     * @param requestDto {@link TodoRequestDto}
     * @param eTag       更新後の {@link Todo} のETagの値
     * @return {@link TodoResponseDto} の {@link ResponseDtoWithETag}
     */
    public ResponseDtoWithETag<TodoResponseDto> createResponseFromUpdateRequest(
        UUID todoId,
        TodoRequestDto requestDto,
        String eTag
    ) {
        TodoResponseDto todo = new TodoResponseDto(
            todoId,
            requestDto.title(),
            requestDto.completed(),
            CREATED_DATE_1,
            UPDATED_DATE_1
        );

        return new ResponseDtoWithETag<>(todo, eTag);
    }
}
