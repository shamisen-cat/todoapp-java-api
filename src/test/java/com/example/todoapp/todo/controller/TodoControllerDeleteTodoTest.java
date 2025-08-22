package com.example.todoapp.todo.controller;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.todoapp.etag.service.ETagValidator;
import com.example.todoapp.todo.service.TodoService;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static com.example.todoapp.testutil.TestConstants.*;

/**
 * {@link TodoController#deleteTodo} のWeb層結合テスト
 * <p>
 * 正常系：
 * <ul>
 *   <li>空のレスポンスボディと 204 No Content が返却されること</li>
 * </ul>
 */
@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerDeleteTodoTest {

    @Autowired
    MockMvc mockMvc;

    /** CRUD操作のビジネスロジックを提供するサービス */
    @MockitoBean
    TodoService todoService;

    /** ETagを検証するバリデータ */
    @MockitoBean
    ETagValidator eTagValidator;

    /**
     * 空のレスポンスボディと 204 No Content が返却されることを検証する。
     */
    @Test
    void shouldReturnNoContent_whenDelete() throws Exception {
        UUID todoId = TODO_ID_1;
        String ifMatch = E_TAG_1;

        mockMvc
            .perform(delete("%s/{id}".formatted(BASE_URL), todoId)
                .header("If-Match", ifMatch)
            )
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));

        verify(eTagValidator, times(1))
            .assertETagPresent(ifMatch, CONTROLLER_DELETE_CONTEXT);
        verifyNoMoreInteractions(eTagValidator);

        verify(todoService, times(1)).deleteTodo(todoId, ifMatch);
        verifyNoMoreInteractions(todoService);
    }
}
