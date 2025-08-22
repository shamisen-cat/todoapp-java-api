package com.example.todoapp.todo.controller;

import java.util.List;
import java.util.stream.Stream;

import jakarta.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.todoapp.common.error.ErrorCode;
import com.example.todoapp.etag.dto.ResponseDtoWithETag;
import com.example.todoapp.testutil.TodoControllerTestFactory;
import com.example.todoapp.todo.dto.TodoResponseDto;
import com.example.todoapp.todo.service.TodoService;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

import static org.hamcrest.Matchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static com.example.todoapp.testutil.TestConstants.*;

/**
 * {@link TodoController#getTodos} のWeb層結合テスト
 * <p>
 * 正常系：
 * <ul>
 *   <li>クエリパラメータに基づいた {@link Page} が返却されること</li>
 *   <li>クエリパラメータの指定がない場合に、デフォルトの {@link Page} が返却されること</li>
 *   <li>ページ番号が総ページ数を超える場合に、空の {@link Page} が返却されること</li>
 * </ul>
 * <p>
 * 異常系：
 * <ul>
 *   <li>指定されたクエリパラメータが無効な場合に、400 Bad Request が返却されること</li>
 *   <li>クエリパラメータが数値ではない場合に、400 Bad Request が返却されること</li>
 * </ul>
 */
@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerGetTodosTest {

    @Autowired
    private MockMvc mockMvc;

    /** テストデータを生成するファクトリ */
    @Autowired
    private TodoControllerTestFactory todoTestDataFactory;

    /** CRUD操作のビジネスロジックを提供するサービス */
    @MockitoBean
    private TodoService todoService;

    /**
     * クエリパラメータに基づいた {@link Page} が返却されることを検証する。
     *
     * @param page 0始まりのページ番号
     * @param size 1ページあたりの件数
     */
    @ParameterizedTest
    @MethodSource("provideValidQueryParams")
    void shouldReturnPage_whenValidQueryParams(int page, int size) throws Exception {
        var response = todoTestDataFactory.createPageResponse(page, size);
        int totalElements = response.getContent().size();

        when(todoService.getTodoPageByUpdatedAt(page, size)).thenReturn(response);

        mockMvc
            .perform(get(BASE_URL)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content.length()").value(totalElements))
            .andExpect(jsonPath("$.totalElements").value(totalElements))
            .andExpect(jsonPath("$.number").value(page))
            .andExpect(jsonPath("$.size").value(size))
            .andExpect(jsonPath("$.pageable.pageNumber").value(page))
            .andExpect(jsonPath("$.pageable.pageSize").value(size));

        verify(todoService, times(1)).getTodoPageByUpdatedAt(page, size);
        verifyNoMoreInteractions(todoService);
    }

    static Stream<Arguments> provideValidQueryParams() {
        return Stream.of(
            // デフォルト値
            Arguments.of(DEFAULT_PAGE, DEFAULT_PAGE_SIZE),

            // 1ページあたりの件数の境界値
            Arguments.of(DEFAULT_PAGE, MIN_PAGE_SIZE),
            Arguments.of(DEFAULT_PAGE, MAX_PAGE_SIZE),
            Arguments.of(DEFAULT_PAGE, (MAX_PAGE_SIZE + MIN_PAGE_SIZE) / 2),

            // 2ページ目以降
            Arguments.of(1, MIN_PAGE_SIZE)
        );
    }

    /**
     * クエリパラメータの指定がない場合に、デフォルトの {@link Page} が返却されることを検証する。
     */
    @Test
    void shouldReturnDefaultPage_whenQueryParamsNotProvided() throws Exception {
        int page = DEFAULT_PAGE;
        int size = DEFAULT_PAGE_SIZE;

        var response = todoTestDataFactory.createPageResponse(page, size);

        when(todoService.getTodoPageByUpdatedAt(page, size)).thenReturn(response);

        mockMvc
            .perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.number").value(page))
            .andExpect(jsonPath("$.size").value(size));

        verify(todoService, times(1)).getTodoPageByUpdatedAt(page, size);
        verifyNoMoreInteractions(todoService);
    }

    /**
     * ページ番号が総ページ数を超える場合に、空の {@link Page} が返却されることを検証する。
     *
     * @param page          0始まりのページ番号
     * @param size          1ページあたりの件数
     * @param totalElements 総件数
     */
    @ParameterizedTest
    @MethodSource("providePageExceedingTotalPages")
    void shouldReturnEmptyPage_whenPageExceedsTotalPages(
        int page,
        int size,
        int totalElements
    ) throws Exception {
        Page<ResponseDtoWithETag<TodoResponseDto>> response = new PageImpl<>(
            List.of(),
            PageRequest.of(page, size),
            totalElements
        );

        when(todoService.getTodoPageByUpdatedAt(page, size)).thenReturn(response);

        mockMvc
            .perform(get(BASE_URL)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content.length()").value(0))
            .andExpect(jsonPath("$.number").value(page))
            .andExpect(jsonPath("$.size").value(size))
            .andExpect(jsonPath("$.pageable.pageNumber").value(page))
            .andExpect(jsonPath("$.pageable.pageSize").value(size));

        verify(todoService, times(1)).getTodoPageByUpdatedAt(page, size);
        verifyNoMoreInteractions(todoService);
    }

    static Stream<Arguments> providePageExceedingTotalPages() {
        return Stream.of(
            Arguments.of(DEFAULT_PAGE, DEFAULT_PAGE_SIZE, 0),
            Arguments.of(1, DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE - 1),
            Arguments.of(2, DEFAULT_PAGE_SIZE, DEFAULT_PAGE_SIZE * 2 - 1)
        );
    }

    /**
     * 指定されたクエリパラメータが無効な場合に、400 Bad Request が返却されることを検証する。
     *
     * @param page 0始まりのページ番号
     * @param size 1ページあたりの件数
     */
    @ParameterizedTest
    @MethodSource("provideInvalidQueryParams")
    void shouldThrowBadRequest_whenInvalidQueryParams(
        int page,
        int size
    ) throws Exception {
        MvcResult result = mockMvc
            .perform(get(BASE_URL)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
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
            .isInstanceOf(ConstraintViolationException.class);

        verify(todoService, never()).getTodoPageByUpdatedAt(page, size);
    }

    static Stream<Arguments> provideInvalidQueryParams() {
        return Stream.of(
            // 0始まりのページ番号または1ページあたりの件数が負の場合
            Arguments.of(-1, DEFAULT_PAGE_SIZE),
            Arguments.of(DEFAULT_PAGE, -1),

            // 1ページあたりの件数の境界値
            Arguments.of(DEFAULT_PAGE, MIN_PAGE_SIZE - 1),
            Arguments.of(DEFAULT_PAGE, MAX_PAGE_SIZE + 1),

            // 1ページあたりの件数が0の場合
            Arguments.of(DEFAULT_PAGE, 0)
        );
    }

    /**
     * クエリパラメータが数値ではない場合に、400 Bad Request が返却されることを検証する。
     *
     * @param paramName  クエリパラメータ名
     * @param paramValue クエリパラメータの値
     */
    @ParameterizedTest
    @MethodSource("provideNotNumberQueryParams")
    void shouldReturnBadRequest_whenNotNumberQueryParam(
        String paramName,
        String paramValue
    ) throws Exception {
        MvcResult result = mockMvc
            .perform(get(BASE_URL).param(paramName, paramValue))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(APPLICATION_PROBLEM_JSON))
            .andExpect(jsonPath("$.errorCode").value(
                ErrorCode.REQUEST_VALIDATION_FAILURE.getCode()
            ))
            .andExpect(jsonPath("$.title").value(
                REQUEST_VALIDATION_ERROR_TITLE
            ))
            .andExpect(jsonPath("$.detail").value(
                ErrorCode.REQUEST_VALIDATION_FAILURE.getMessage().formatted(paramName)
            ))
            .andReturn();

        assertThat(result.getResolvedException())
            .isInstanceOf(MethodArgumentTypeMismatchException.class);

        verify(todoService, never()).getTodoPageByUpdatedAt(anyInt(), anyInt());
    }

    static Stream<Arguments> provideNotNumberQueryParams() {
        return Stream.of(
            Arguments.of("page", NON_NUMERIC_QUERY_PARAM),
            Arguments.of("size", NON_NUMERIC_QUERY_PARAM)
        );
    }
}
