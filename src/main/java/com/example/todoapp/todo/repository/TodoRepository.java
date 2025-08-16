package com.example.todoapp.todo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todoapp.todo.model.Todo;

/**
 * {@link Todo} のデータベースにアクセスするリポジトリ
 * <p>
 * CRUD操作をするための {@link JpaRepository} を継承する。
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, UUID> {
    // 必要に応じて独自のクエリメソッドを定義する。
}
