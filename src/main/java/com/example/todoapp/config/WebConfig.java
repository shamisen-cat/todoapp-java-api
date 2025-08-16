package com.example.todoapp.config;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * アプリケーションのMVC設定
 */
@Configuration
public class WebConfig implements WebMvcConfigurer{

    /** 有効なプロファイル名 */
    @Value("${spring.profiles.active:}")
    private String activeProfile;

    /**
     * CORS設定を追加する。
     *
     * @param registry CORS設定を登録・管理するためのレジストリ
     */
    @Override
    public void addCorsMappings(
        @NonNull
        CorsRegistry registry
    ) {
        String allowedOrigins = switch (activeProfile) {
            case "prod" -> "http://localhost:5173";
            default -> "http://localhost:5173";
        };

        registry
            .addMapping("/api/**")
            .allowedOrigins(allowedOrigins)
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("ETag")
            .allowCredentials(true)
            .maxAge(600);
    }

    /**
     * JSON変換用のコンバータに対して、デフォルトの文字コードをUTF-8に設定する。
     *
     * @param converters HTTPメッセージコンバータのリスト
     */
    @Override
    public void extendMessageConverters(
        @NonNull
        List<HttpMessageConverter<?>> converters
    ) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof
                MappingJackson2HttpMessageConverter jacksonConverter) {
                jacksonConverter.setDefaultCharset(StandardCharsets.UTF_8);
            }
        }
    }
}
