package com.example.todoapp.todo.service.factory.normalize;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.example.todoapp.config.TodoProperties;
import com.example.todoapp.todo.dto.TodoRequest;
import com.example.todoapp.todo.service.factory.validation.TodoValidator;

/**
 * {@link TodoRequest#title} の正規化を行うクラス
 */
@Component
@RequiredArgsConstructor
public class TitleNormalizer {

    /** フィールド名 */
    private static final String FIELD_NAME = "title";

    private final TodoValidator todoValidator;
    private final TodoProperties todoProperties;

    /**
     * {@link TodoRequest#title} を正規化する。
     *
     * @param title {@link TodoRequest#title}
     * @return 正規化されたタイトル
     */
    public String normalize(String title) {
        todoValidator.assertNotNull(FIELD_NAME, title);
        String trimmed = title.trim();
        todoValidator.assertNotBlank(FIELD_NAME, trimmed);
        todoValidator.assertMaxLength(
            FIELD_NAME,
            trimmed,
            todoProperties.getTitleMaxLength()
        );

        return trimmed;
    }
}
