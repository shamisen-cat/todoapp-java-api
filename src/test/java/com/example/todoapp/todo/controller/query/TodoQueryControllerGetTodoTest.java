package com.example.todoapp.todo.controller.query;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.todoapp.testutil.TodoResponseFixture;
import com.example.todoapp.todo.dto.TodoResponse;
import com.example.todoapp.todo.service.query.TodoQueryService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static com.example.todoapp.testutil.TestConstants.*;

/**
 * {@link TodoQueryController#getTodo} のWeb層結合テスト
 * <p>
 * 正常系：
 * <ul>
 *   <li>IDに基づいた {@link TodoResponse} を含む {@link ETagResponse} が返却されること</li>
 * </ul>
 */
@SpringBootTest
@AutoConfigureMockMvc
class TodoQueryControllerGetTodoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoResponseFixture todoResponseFixture;

    @MockitoBean
    private TodoQueryService todoQueryService;

    /**
     * IDに基づいた {@link TodoResponse} を含む {@link ETagResponse} が返却されることを検証する。
     */
    @Test
    void shouldReturnResponse_whenValidId() throws Exception {
        // Arrange
        UUID id = TODO_ID_1;

        var response = todoResponseFixture.create(
            id,
            VALID_TITLE_1,
            false,
            CREATED_DATE_1,
            UPDATED_DATE_1,
            E_TAG_1
        );
        TodoResponse todoResponse = response.data();

        when(todoQueryService.getTodo(id)).thenReturn(response);

        // Act
        mockMvc
            .perform(get("%s/{id}".formatted(BASE_URL), id))

            // Assert
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(todoResponse.id().toString()))
            .andExpect(jsonPath("$.title").value(todoResponse.title()))
            .andExpect(jsonPath("$.completed").value(todoResponse.completed()))
            .andExpect(jsonPath("$.createdAt").value(
                todoResponse.createdAt().format(DATE_TIME_FORMATTER)
            ))
            .andExpect(jsonPath("$.updatedAt").value(
                todoResponse.updatedAt().format(DATE_TIME_FORMATTER)
            ))
            .andExpect(header().string("ETag", "%s".formatted(response.etag())));

        // Assert: Mock
        verify(todoQueryService, times(1)).getTodo(id);
        verifyNoMoreInteractions(todoQueryService);
    }
}
