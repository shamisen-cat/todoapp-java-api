package com.example.todoapp.todo.controller.command;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.todoapp.etag.validation.ETagValidator;
import com.example.todoapp.todo.service.command.TodoCommandService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static com.example.todoapp.testutil.TestConstants.*;

/**
 * {@link TodoCommandController#deleteTodo} のWeb層結合テスト
 * <p>
 * 正常系：
 * <ul>
 *   <li>空のレスポンスボディと204 No Contentが返却されること</li>
 * </ul>
 */
@SpringBootTest
@AutoConfigureMockMvc
class TodoCommandControllerDeleteTodoTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TodoCommandService todoCommandService;

    @MockitoBean
    ETagValidator eTagValidator;

    /**
     * 空のレスポンスボディと204 No Contentが返却されることを検証する。
     */
    @Test
    void shouldReturnNoContent_whenDelete() throws Exception {
        // Arrange
        UUID id = TODO_ID_1;
        String ifMatch = E_TAG_1;

        // Act
        mockMvc
            .perform(delete("%s/{id}".formatted(BASE_URL), id)
                .header("If-Match", ifMatch)
            )

            // Assert
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));

        // Assert: Mock
        verify(eTagValidator, times(1)).assertETagPresent(ifMatch);
        verify(todoCommandService, times(1)).deleteTodo(id, ifMatch);
        verifyNoMoreInteractions(eTagValidator, todoCommandService);
    }
}
