package com.example.todoapp.testutil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.http.MediaType;

/**
 * テストで使用する定数の定義
 */
public final class TestConstants {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static final MediaType APPLICATION_PROBLEM_JSON = MediaType.parseMediaType("application/problem+json");

    // --- リクエスト ---

    public static final String BASE_URL = "/api/todos";

    public static final String NON_NUMERIC_QUERY_PARAM = "xyz";

    public static final String REQUEST_VALIDATION_ERROR_TITLE  = "Request Validation Failure";
    public static final String REQUEST_VALIDATION_ERROR_DETAIL = "Request validation failed for field";

    // -- レスポンス ---

    public static final UUID TODO_ID_1 = UUID.fromString("11111111-1111-4111-8111-111111111111");
    public static final UUID TODO_ID_2 = UUID.fromString("22222222-2222-4222-8222-222222222222");

    public static final String VALID_TITLE_1 = "Test Title 1";
    public static final String VALID_TITLE_2 = " Test Title 2 ";
    public static final String VALID_TITLE_3 = "Test Title 3　";
    public static final String VALID_TITLE_4 = "Test Title 4\t";
    public static final String VALID_TITLE_5 = "\nTest Title 5\n";
    public static final String VALID_TITLE_6 = "Test Title 6\n\n";
    public static final String VALID_TITLE_7 = "Test\nTitle 7\n";
    public static final String VALID_TITLE_8 = "Test\r\nTitle 8\r\n";

    public static final String BLANK_TITLE_1  = "";
    public static final String BLANK_TITLE_2  = " ";
    public static final String BLANK_TITLE_3  = "　";
    public static final String BLANK_TITLE_4  = "\t";
    public static final String BLANK_TITLE_5  = "\n";
    public static final String BLANK_TITLE_6  = "  ";
    public static final String BLANK_TITLE_7  = " 　";
    public static final String BLANK_TITLE_8  = " \t";
    public static final String BLANK_TITLE_9  = " \n";
    public static final String BLANK_TITLE_10 = "\n ";
    public static final String BLANK_TITLE_11 = "\n\n";
    public static final String BLANK_TITLE_12 = "\r\n";
    public static final String BLANK_TITLE_13 = " \n ";
    public static final String BLANK_TITLE_14 = "\n \n";

    public static final LocalDateTime CREATED_DATE_1 = LocalDateTime.of(2024, 12, 31,  0,  0,  0);
    public static final LocalDateTime UPDATED_DATE_1 = LocalDateTime.of(2025,  1,  1,  6, 15, 15);

    public static final LocalDateTime CREATED_DATE_2 = LocalDateTime.of(2024,  3, 31, 12, 30, 30);
    public static final LocalDateTime UPDATED_DATE_2 = LocalDateTime.of(2024,  4,  1, 18, 45, 45);

    public static final String E_TAG_1 = "\"etag_1\"";
    public static final String E_TAG_2 = "\"etag_2\"";

    // --- ページング ---

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final int MIN_PAGE_SIZE = 1;
    public static final int MAX_PAGE_SIZE = 100;
}
