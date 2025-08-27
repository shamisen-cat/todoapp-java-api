package com.example.todoapp.todo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todoapp.todo.model.TodoEntity;

/**
 * To-doのデータベースにアクセスするリポジトリインタフェース
 * <p>
 * CRUD操作を提供する {@link JpaRepository} を継承
 */
@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, UUID> {
    // 必要に応じてクエリメソッドを定義
}
