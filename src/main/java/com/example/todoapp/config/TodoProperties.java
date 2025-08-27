package com.example.todoapp.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * To-doの定数設定
 */
@Component
@ConfigurationProperties(prefix = "todo")
@Getter
@Setter
public class TodoProperties {

    /** タイトルの最大文字数 */
    @Min(1)
    @Max(100)
    private int titleMaxLength = 100;
}
