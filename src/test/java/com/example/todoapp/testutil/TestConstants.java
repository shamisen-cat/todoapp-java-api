package com.example.todoapp.testutil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;

/**
 * テストで使用する定数を定義するクラス
 */
public final class TestConstants {

    /** 日時のフォーマッタ（yyyy-MM-dd'T'HH:mm:ss） */
    public static final DateTimeFormatter FORMATTER_WITH_SECONDS = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // --- リクエスト ---

    /** ベースURL */
    public static final String BASE_URL = "/api/todos";

    /** クエリパラメータが数値ではない場合のテスト用文字列 */
    public static final String NON_NUMERIC_QUERY_PARAM = "xyz";

    /** リクエストの検証に失敗した場合の {@link ProblemDetail#title} の文字列 */
    public static final String REQUEST_VALIDATION_ERROR_TITLE = "Request Validation Failure";
    /** リクエストの検証に失敗した場合の {@link ProblemDetail#detail} の文字列 */
    public static final String REQUEST_VALIDATION_ERROR_DETAIL = "Request validation failed for field";

    // -- レスポンス ---

    /** UUID.fromString("11111111-1111-4111-8111-111111111111") */
    public static final UUID TODO_ID_1 = UUID.fromString("11111111-1111-4111-8111-111111111111");
    /** UUID.fromString("22222222-2222-4222-8222-222222222222") */
    public static final UUID TODO_ID_2 = UUID.fromString("22222222-2222-4222-8222-222222222222");

    /** "Test Title 1" */
    public static final String VALID_TITLE_1 = "Test Title 1";
    /** " Test Title 2 " */
    public static final String VALID_TITLE_2 = " Test Title 2 ";
    /** "Test Title 3　" */
    public static final String VALID_TITLE_3 = "Test Title 3　";
    /** "Test Title 4\t" */
    public static final String VALID_TITLE_4 = "Test Title 4\t";
    /** "\nTest Title 5\n" */
    public static final String VALID_TITLE_5 = "\nTest Title 5\n";
    /** "Test Title 6\n\n" */
    public static final String VALID_TITLE_6 = "Test Title 6\n\n";
    /** "Test\nTitle 7\n" */
    public static final String VALID_TITLE_7 = "Test\nTitle 7\n";
    /** "Test\r\nTitle 8\r\n" */
    public static final String VALID_TITLE_8 = "Test\r\nTitle 8\r\n";

    /** "" */
    public static final String INVALID_TITLE_1  = "";
    /** " " */
    public static final String INVALID_TITLE_2  = " ";
    /** "　" */
    public static final String INVALID_TITLE_3  = "　";
    /** "\t" */
    public static final String INVALID_TITLE_4  = "\t";
    /** "\n" */
    public static final String INVALID_TITLE_5  = "\n";
    /** "  " */
    public static final String INVALID_TITLE_6  = "  ";
    /** " 　" */
    public static final String INVALID_TITLE_7  = " 　";
    /** " \t" */
    public static final String INVALID_TITLE_8  = " \t";
    /** " \n" */
    public static final String INVALID_TITLE_9  = " \n";
    /** "\n " */
    public static final String INVALID_TITLE_10 = "\n ";
    /** "\n\n" */
    public static final String INVALID_TITLE_11 = "\n\n";
    /** "\r\n" */
    public static final String INVALID_TITLE_12 = "\r\n";
    /** " \n " */
    public static final String INVALID_TITLE_13 = " \n ";
    /** "\n \n" */
    public static final String INVALID_TITLE_14 = "\n \n";

    /** LocalDateTime.of(2023, 12, 31,  0,  0,  0) */
    public static final LocalDateTime CREATED_DATE_1 = LocalDateTime.of(2023, 12, 31,  0,  0,  0);
    /** LocalDateTime.of(2024,  1,  1,  6, 15, 15) */
    public static final LocalDateTime UPDATED_DATE_1 = LocalDateTime.of(2024,  1,  1,  6, 15, 15);

    /** LocalDateTime.of(2023,  3, 31, 12, 30, 30) */
    public static final LocalDateTime CREATED_DATE_2 = LocalDateTime.of(2023,  3, 31, 12, 30, 30);
    /** LocalDateTime.of(2024,  4,  1, 18, 45, 45) */
    public static final LocalDateTime UPDATED_DATE_2 = LocalDateTime.of(2024,  4,  1, 18, 45, 45);

    /** "\"etag_1\" */
    public static final String E_TAG_1 = "\"etag_1\"";
    /** "\"etag_2\" */
    public static final String E_TAG_2 = "\"etag_2\"";

    /** エラーレスポンスの Content-Type */
    public static final MediaType APPLICATION_PROBLEM_JSON = MediaType.parseMediaType("application/problem+json");

    // --- ページング ---

    /** 0始まりのページ番号のデフォルト値 */
    public static final int DEFAULT_PAGE = 0;
    /** 1ページあたりの件数のデフォルト値 */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /** 1ページあたりの件数の最小値 */
    public static final int MIN_PAGE_SIZE = 1;
    /** 1ページあたりの件数の最大値 */
    public static final int MAX_PAGE_SIZE = 100;

    // --- コンテキスト ---

    /** "TodoController#updateTodo" */
    public static final String CONTROLLER_UPDATE_CONTEXT = "TodoController#updateTodo";
    /** "TodoController#deleteTodo" */
    public static final String CONTROLLER_DELETE_CONTEXT = "TodoController#deleteTodo";
}
