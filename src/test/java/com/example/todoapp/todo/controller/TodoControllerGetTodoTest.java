package com.example.todoapp.todo.controller;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.todoapp.etag.dto.ResponseDtoWithETag;
import com.example.todoapp.testutil.TodoControllerTestFactory;
import com.example.todoapp.todo.dto.TodoResponseDto;
import com.example.todoapp.todo.service.TodoService;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static com.example.todoapp.testutil.TestConstants.*;

/**
 * {@link TodoController#getTodo} のWeb層結合テスト
 * <p>
 * 正常系：
 * <ul>
 *   <li>IDに基づいた {@link ResponseDtoWithETag} が返却されること</li>
 * </ul>
 */
@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerGetTodoTest {

    @Autowired
    private MockMvc mockMvc;

    /** テストデータを生成するファクトリ */
    @Autowired
    private TodoControllerTestFactory todoTestDataFactory;

    /** CRUD操作のビジネスロジックを提供するサービス */
    @MockitoBean
    private TodoService todoService;

    /**
     * IDに基づいた {@link ResponseDtoWithETag} が返却されることを検証する。
     */
    @Test
    void shouldReturnResponse_whenValidTodoId() throws Exception {
        UUID todoId = TODO_ID_1;

        var response = todoTestDataFactory.createResponseFromId(todoId);
        TodoResponseDto responseDto = response.data();

        when(todoService.getTodo(todoId)).thenReturn(response);

        mockMvc
            .perform(get("%s/{id}".formatted(BASE_URL), todoId))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(responseDto.id().toString()))
            .andExpect(jsonPath("$.title").value(responseDto.title()))
            .andExpect(jsonPath("$.completed").value(responseDto.completed()))
            .andExpect(jsonPath("$.createdAt").value(
                responseDto.createdAt().format(FORMATTER_WITH_SECONDS)
            ))
            .andExpect(jsonPath("$.updatedAt").value(
                responseDto.updatedAt().format(FORMATTER_WITH_SECONDS)
            ))
            .andExpect(header().string(
                "ETag",
                "%s".formatted(response.etag())
            ));

        verify(todoService, times(1)).getTodo(todoId);
        verifyNoMoreInteractions(todoService);
    }
}
