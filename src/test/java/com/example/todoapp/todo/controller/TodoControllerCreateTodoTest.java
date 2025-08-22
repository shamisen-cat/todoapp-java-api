package com.example.todoapp.todo.controller;

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
import com.example.todoapp.etag.dto.ResponseDtoWithETag;
import com.example.todoapp.testutil.TodoControllerTestFactory;
import com.example.todoapp.todo.dto.TodoRequestDto;
import com.example.todoapp.todo.dto.TodoResponseDto;
import com.example.todoapp.todo.service.TodoService;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static com.example.todoapp.testutil.TestConstants.*;

/**
 * {@link TodoController#createTodo} のWeb層結合テスト
 * <p>
 * 正常系：
 * <ul>
 *   <li>{@link TodoRequestDto} に基づいた {@link ResponseDtoWithETag} と HTTP 201 Created が返却されること</li>
 * </ul>
 * <p>
 * 異常系：
 * <ul>
 *   <li>{@link TodoRequestDto} が空の場合に、400 Bad Request が返却されること</li>
 *   <li>{@link TodoRequestDto#title} が {@code null} の場合に、400 Bad Request が返却されること</li>
 *   <li>{@link TodoRequestDto#title} が空文字列の場合に、400 Bad Request が返却されること</li>
 * </ul>
 */
@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerCreateTodoTest {

    @Autowired
    MockMvc mockMvc;

    /** テストデータを生成するファクトリ */
    @Autowired
    TodoControllerTestFactory todoTestDataFactory;

    /** JSONの入出力変換をするマッパー */
    @Autowired
    ObjectMapper objectMapper;

    /** CRUD操作のビジネスロジックを提供するサービス */
    @MockitoBean
    TodoService todoService;

    /**
     * {@link TodoRequestDto} に基づいた {@link ResponseDtoWithETag} と HTTP 201 Created が返却されることを検証する。
     *
     * @param title {@link TodoRequestDto#title}
     */
    @ParameterizedTest
    @MethodSource("provideValidTitles")
    void shouldReturnCreated_whenValidTitle(String title) throws Exception {
        TodoRequestDto requestDto = new TodoRequestDto(title, null);
        var response = todoTestDataFactory.createResponseFromCreateRequest(requestDto);
        TodoResponseDto responseDto = response.data();

        when(todoService.createTodo(eq(requestDto))).thenReturn(response);

        mockMvc
            .perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
            )
            .andExpect(status().isCreated())
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
            ))
            .andExpect(header().string(
                "Location",
                containsString(
                    "%s/%s".formatted(BASE_URL, responseDto.id())
                )
            ));

        verify(todoService, times(1)).createTodo(eq(requestDto));
        verifyNoMoreInteractions(todoService);
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
     * {@link TodoRequestDto} が空の場合に、400 Bad Request が返却されることを検証する。
     */
    @Test
    void shouldReturnBadRequest_whenRequestBodyIsEmpty() throws Exception {
        MvcResult result = mockMvc
            .perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
            )
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("$.errorCode").value(
                ErrorCode.REQUEST_VALIDATION_FAILURE.getCode()
            ))
            .andExpect(jsonPath("$.title").value(
                REQUEST_VALIDATION_ERROR_TITLE
            ))
            .andExpect(jsonPath("$.detail").value(
                containsString(REQUEST_VALIDATION_ERROR_DETAIL)
            ))
            .andReturn();

        assertThat(result.getResolvedException())
            .isInstanceOf(MethodArgumentNotValidException.class);

        verify(todoService, never()).createTodo(any());
    }

    /**
     * {@link TodoRequestDto#title} が {@code null} の場合に、400 Bad Request が返却されることを検証する。
     */
    @Test
    void shouldReturnBadRequest_whenTitleIsNull() throws Exception {
        TodoRequestDto requestDto = new TodoRequestDto(null, null);

        MvcResult result = mockMvc
            .perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
            )
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("$.errorCode").value(
                ErrorCode.REQUEST_VALIDATION_FAILURE.getCode()
            ))
            .andExpect(jsonPath("$.title").value(
                REQUEST_VALIDATION_ERROR_TITLE
            ))
            .andExpect(jsonPath("$.detail").value(
                containsString(REQUEST_VALIDATION_ERROR_DETAIL)
            ))
            .andReturn();

        assertThat(result.getResolvedException())
            .isInstanceOf(MethodArgumentNotValidException.class);

        verify(todoService, never()).createTodo(eq(requestDto));
    }

    /**
     * {@link TodoRequestDto#title} が空文字列の場合に、400 Bad Request が返却されることを検証する。
     *
     * @param title {@link TodoRequestDto#title}
     */
    @ParameterizedTest
    @MethodSource("provideBlankTitles")
    void shouldReturnBadRequest_whenTitleIsBlank(String title) throws Exception {
        TodoRequestDto requestDto = new TodoRequestDto(title, null);

        MvcResult result = mockMvc
            .perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
            )
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("$.errorCode").value(
                ErrorCode.REQUEST_VALIDATION_FAILURE.getCode()
            ))
            .andExpect(jsonPath("$.title").value(
                REQUEST_VALIDATION_ERROR_TITLE
            ))
            .andExpect(jsonPath("$.detail").value(
                containsString(REQUEST_VALIDATION_ERROR_DETAIL)
            ))
            .andReturn();

        assertThat(result.getResolvedException())
            .isInstanceOf(MethodArgumentNotValidException.class);

        verify(todoService, never()).createTodo(eq(requestDto));
    }

    static Stream<String> provideBlankTitles() {
        return Stream.of(
            INVALID_TITLE_1,
            INVALID_TITLE_2,
            INVALID_TITLE_3,
            INVALID_TITLE_4,
            INVALID_TITLE_5,
            INVALID_TITLE_6,
            INVALID_TITLE_7,
            INVALID_TITLE_8,
            INVALID_TITLE_9,
            INVALID_TITLE_10,
            INVALID_TITLE_11,
            INVALID_TITLE_12,
            INVALID_TITLE_13,
            INVALID_TITLE_14
        );
    }
}
