package com.example.todoapp.todo.controller.command;

import java.util.UUID;
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
import com.example.todoapp.etag.validation.ETagValidator;
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
 * {@link TodoCommandController#updateTodo} のWeb層結合テスト
 * <p>
 * 正常系：
 * <ul>
 *   <li>{@link TodoRequest} に基づいた {@link ETagResponse} が返却されること</li>
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
class TodoCommandControllerUpdateTodoTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TodoResponseFixture todoResponseFixture;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    TodoCommandService todoCommandService;

    @MockitoBean
    ETagValidator eTagValidator;

    /**
     * {@link TodoRequest} に基づいた {@link ETagResponse} が返却されることを検証する。
     *
     * @param title {@link TodoRequest#title}
     */
    @ParameterizedTest
    @MethodSource("provideValidTitles")
    void shouldReturnResponse_whenValidTitle(String title) throws Exception {
        // Arrange
        UUID id = TODO_ID_1;
        String ifMatch = E_TAG_1;

        TodoRequest request = new TodoRequest(title, false);
        var response = todoResponseFixture.create(
            TODO_ID_1,
            request.title(),
            request.completed(),
            CREATED_DATE_1,
            UPDATED_DATE_1,
            E_TAG_2
        );
        TodoResponse todoResponse = response.data();

        doNothing().when(eTagValidator).assertETagPresent(ifMatch);
        when(todoCommandService.updateTodo(eq(id), eq(request), eq(ifMatch)))
            .thenReturn(response);

        // Act
        mockMvc
            .perform(put("%s/{id}".formatted(BASE_URL), id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("If-Match", ifMatch)
            )

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
        verify(eTagValidator, times(1)).assertETagPresent(ifMatch);
        verify(todoCommandService, times(1))
            .updateTodo(eq(id), eq(request), eq(ifMatch));
        verifyNoMoreInteractions(eTagValidator, todoCommandService);
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
        // Arrange
        UUID id = TODO_ID_1;
        String ifMatch = E_TAG_1;

        // Act
        MvcResult result = mockMvc
            .perform(put("%s/{id}".formatted(BASE_URL), id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .header("If-Match", ifMatch)
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
        verify(eTagValidator, never()).assertETagPresent(ifMatch);
        verify(todoCommandService, never()).updateTodo(eq(id), any(), eq(ifMatch));
        verifyNoMoreInteractions(eTagValidator, todoCommandService);
    }

    /**
     * {@link TodoRequest#title} が {@code null} の場合に、400 Bad Requestが返却されることを検証する。
     */
    @Test
    void shouldReturnBadRequest_whenTitleIsNull() throws Exception {
        // Arrange
        UUID id = TODO_ID_1;
        TodoRequest request = new TodoRequest(null, false);
        String ifMatch = E_TAG_1;

        // Act
        MvcResult result = mockMvc
            .perform(put("%s/{id}".formatted(BASE_URL), id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("If-Match", ifMatch)
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
        verify(eTagValidator, never()).assertETagPresent(ifMatch);
        verify(todoCommandService, never())
            .updateTodo(eq(id), eq(request), eq(ifMatch));
        verifyNoMoreInteractions(eTagValidator, todoCommandService);
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
        UUID id = TODO_ID_1;
        String ifMatch = E_TAG_1;

        TodoRequest request = new TodoRequest(title, false);

        // Act
        MvcResult result = mockMvc
            .perform(put("%s/{id}".formatted(BASE_URL), id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("If-Match", ifMatch)
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
        verify(eTagValidator, never()).assertETagPresent(ifMatch);
        verify(todoCommandService, never())
            .updateTodo(eq(id), eq(request), eq(ifMatch));
        verifyNoMoreInteractions(eTagValidator, todoCommandService);
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
