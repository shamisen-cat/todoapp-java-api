package com.example.todoapp.todo.controller.command;

import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.example.todoapp.common.error.ErrorCode;
import com.example.todoapp.etag.dto.ETagResponse;
import com.example.todoapp.testutil.TodoResponseFixture;
import com.example.todoapp.todo.dto.TodoRequest;
import com.example.todoapp.todo.dto.TodoResponse;
import com.example.todoapp.todo.service.command.TodoCommandService;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static com.example.todoapp.testutil.TestConstants.*;

/**
 * {@link TodoCommandController#createTodo} のWeb層結合テスト
 * <p>
 * 正常系：
 * <ul>
 *   <li>{@link TodoRequest} に基づいたHTTP 201 Created {@link ETagResponse} が返却されること</li>
 * </ul>
 * <p>
 * 異常系：
 * <ul>
 *   <li>{@link TodoRequest} が空の場合に、400 Bad Requestが返却されること</li>
 *   <li>{@link TodoRequest#title} が {@code null} の場合に、400 Bad Requestが返却されること</li>
 *   <li>{@link TodoRequest#title} が {@code blank} の場合に、400 Bad Requestが返却されること</li>
 * </ul>
 */
@SpringBootTest
@AutoConfigureMockMvc
class TodoCommandControllerCreateTodoTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TodoResponseFixture todoResponseFixture;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    TodoCommandService todoCommandService;

    /**
     * {@link TodoRequest} に基づいたHTTP 201 Created {@link ETagResponse} が返却されることを検証する。
     *
     * @param title {@link TodoRequest#title}
     */
    @ParameterizedTest
    @MethodSource("provideValidTitles")
    void shouldReturnCreated_whenValidTitle(String title) throws Exception {
        // Arrange
        TodoRequest request = new TodoRequest(title, null);

        var response = todoResponseFixture.create(
            TODO_ID_1,
            request.title(),
            false,
            CREATED_DATE_1,
            CREATED_DATE_1,
            E_TAG_1
        );
        TodoResponse todoResponse = response.data();

        when(todoCommandService.createTodo(eq(request))).thenReturn(response);

        // Act
        mockMvc
            .perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            )

            // Assert
            .andExpect(status().isCreated())
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
            .andExpect(header().string("ETag", "%s".formatted(response.etag())))
            .andExpect(header().string(
                "Location",
                containsString("%s/%s".formatted(BASE_URL, todoResponse.id()))
            ));

        // Assert: Mock
        verify(todoCommandService, times(1)).createTodo(eq(request));
        verifyNoMoreInteractions(todoCommandService);
    }
    static Stream<String> provideValidTitles() {
        return Stream.of(
            VALID_TITLE_1,
            VALID_TITLE_2,
            VALID_TITLE_3,
            VALID_TITLE_4,
            VALID_TITLE_5,
            VALID_TITLE_6,
            VALID_TITLE_7,
            VALID_TITLE_8
        );
    }

    /**
     * {@link TodoRequest} が空の場合に、400 Bad Requestが返却されることを検証する。
     */
    @Test
    void shouldReturnBadRequest_whenRequestBodyIsEmpty() throws Exception {
        // Act
        MvcResult result = mockMvc
            .perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
            )

            // Assert
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("$.errorCode").value(
                ErrorCode.REQUEST_VALIDATION_FAILURE.getErrorCode()
            ))
            .andExpect(jsonPath("$.title").value(REQUEST_VALIDATION_ERROR_TITLE))
            .andExpect(jsonPath("$.detail").value(
                containsString(REQUEST_VALIDATION_ERROR_DETAIL)
            ))
            .andReturn();

        // Assert: Exception
        assertThat(result.getResolvedException())
            .isInstanceOf(MethodArgumentNotValidException.class);

        // Assert: Mock
        verify(todoCommandService, never()).createTodo(any());
        verifyNoMoreInteractions(todoCommandService);
    }

    /**
     * {@link TodoRequest#title} が {@code null} の場合に、400 Bad Requestが返却されることを検証する。
     */
    @Test
    void shouldReturnBadRequest_whenTitleIsNull() throws Exception {
        // Arrange
        TodoRequest request = new TodoRequest(null, null);

        // Act
        MvcResult result = mockMvc
            .perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            )

            // Assert
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("$.errorCode").value(
                ErrorCode.REQUEST_VALIDATION_FAILURE.getErrorCode()
            ))
            .andExpect(jsonPath("$.title").value(REQUEST_VALIDATION_ERROR_TITLE))
            .andExpect(jsonPath("$.detail").value(
                containsString(REQUEST_VALIDATION_ERROR_DETAIL)
            ))
            .andReturn();

        // Assert: Exception
        assertThat(result.getResolvedException())
            .isInstanceOf(MethodArgumentNotValidException.class);

        // Assert: Mock
        verify(todoCommandService, never()).createTodo(eq(request));
        verifyNoMoreInteractions(todoCommandService);
    }

    /**
     * {@link TodoRequest#title} が {@code blank} の場合に、400 Bad Requestが返却されることを検証する。
     *
     * @param title {@link TodoRequest#title}
     */
    @ParameterizedTest
    @MethodSource("provideBlankTitles")
    void shouldReturnBadRequest_whenTitleIsBlank(String title) throws Exception {
        // Arrange
        TodoRequest request = new TodoRequest(title, null);

        // Act
        MvcResult result = mockMvc
            .perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            )

            // Assert
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("$.errorCode").value(
                ErrorCode.REQUEST_VALIDATION_FAILURE.getErrorCode()
            ))
            .andExpect(jsonPath("$.title").value(REQUEST_VALIDATION_ERROR_TITLE))
            .andExpect(jsonPath("$.detail").value(
                containsString(REQUEST_VALIDATION_ERROR_DETAIL)
            ))
            .andReturn();

        // Assert: Exception
        assertThat(result.getResolvedException())
            .isInstanceOf(MethodArgumentNotValidException.class);

        // Assert: Mock
        verify(todoCommandService, never()).createTodo(eq(request));
        verifyNoMoreInteractions(todoCommandService);
    }
    static Stream<String> provideBlankTitles() {
        return Stream.of(
            BLANK_TITLE_1,
            BLANK_TITLE_2,
            BLANK_TITLE_3,
            BLANK_TITLE_4,
            BLANK_TITLE_5,
            BLANK_TITLE_6,
            BLANK_TITLE_7,
            BLANK_TITLE_8,
            BLANK_TITLE_9,
            BLANK_TITLE_10,
            BLANK_TITLE_11,
            BLANK_TITLE_12,
            BLANK_TITLE_13,
            BLANK_TITLE_14
        );
    }
}
