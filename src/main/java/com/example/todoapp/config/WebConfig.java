package com.example.todoapp.config;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * HTTPメッセージコンバータ設定
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * JSON変換用コンバータのデフォルト文字コードをUTF-8に設定する。
     */
    @Override
    public void extendMessageConverters(
        @NonNull
        List<HttpMessageConverter<?>> converters
    ) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter jacksonConverters) {
                jacksonConverters.setDefaultCharset(StandardCharsets.UTF_8);
            }
        }
    }
}
