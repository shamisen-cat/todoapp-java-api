package com.example.todoapp.config;

import jakarta.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * 設定値(app.todo.*)を保持するプロパティクラス
 */
@ConfigurationProperties(prefix = "app.todo")
@Validated
public final class TodoProperties {

    /**
     * ToDoのタイトルが未入力の場合のデフォルトタイトル
     */
    @NotBlank(message = "{todo.default-title.required}")
    private String defaultTitle;

    public String getDefaultTitle() {
        return defaultTitle;
    }

    public void setDefaultTitle(String defaultTitle) {
        this.defaultTitle = defaultTitle;
    }
}
