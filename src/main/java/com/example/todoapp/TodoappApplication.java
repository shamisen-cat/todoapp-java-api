package com.example.todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.todoapp.config.TodoProperties;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(TodoProperties.class)
public class TodoappApplication {

    /**
     * アプリケーションのエントリポイント
     *
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        SpringApplication.run(TodoappApplication.class, args);
    }
}
