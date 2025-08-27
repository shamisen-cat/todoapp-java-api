package com.example.todoapp.testutil;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.example.todoapp.etag.dto.ETagResponse;
import com.example.todoapp.todo.dto.TodoResponse;

import static com.example.todoapp.testutil.TestConstants.*;

/**
 * テストで使用する {@link TodoResponse} の {@link Page} を生成するクラス
 */
@Component
public class TodoPageResponseFixture {

    private final TodoResponseFixture todoResponseFixture;

    public TodoPageResponseFixture(TodoResponseFixture todoResponseFixture) {
        this.todoResponseFixture = todoResponseFixture;
    }

    /**
     * 指定されたページ番号と表示件数の {@link Page} を生成する。
     *
     * @param page ページ番号
     * @param size 表示件数
     * @return {@link ETagResponse} の {@link Page}
     */
    public Page<ETagResponse<TodoResponse>> create(int page, int size) {
        List<ETagResponse<TodoResponse>> todos = List.of(
            todoResponseFixture.create(
                TODO_ID_1,
                VALID_TITLE_1,
                true,
                CREATED_DATE_1,
                UPDATED_DATE_1,
                E_TAG_1
            ),
            todoResponseFixture.create(
                TODO_ID_2,
                VALID_TITLE_2,
                false,
                CREATED_DATE_2,
                UPDATED_DATE_2,
                E_TAG_2
            )
        );

        return new PageImpl<>(todos, PageRequest.of(page, size), todos.size());
    }
}
