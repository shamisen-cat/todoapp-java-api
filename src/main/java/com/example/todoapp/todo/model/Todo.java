package com.example.todoapp.todo.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.example.todoapp.common.audit.Auditable;
import com.example.todoapp.etag.factory.ETagSource;

/**
 * {@link Todo} を表すエンティティ
 * <p>
 * 監査情報（作成・更新日時）の管理機能を提供する {@link Auditable} を継承する。
 */
@Entity
@Table(name = "todos")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public final class Todo extends Auditable implements ETagSource {

    /** データベースで自動生成される一意の識別子 */
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    private UUID id;

    /** タイトル */
    @Column(name = "title", nullable = false, length = 100)
    @ToString.Include
    private String title;

    /** 完了状態（true: 完了、false: 未完了） */
    @Column(name = "completed", nullable = false)
    private boolean completed;

    /**
     * 指定されたタイトルの {@link Todo} を生成する。
     *
     * @param title タイトル
     */
    public Todo(String title) {
        this(title, false);
    }

    /**
     * 指定されたタイトルと完了状態の {@link Todo} を生成する。
     *
     * @param title     タイトル
     * @param completed 完了状態（true: 完了、false: 未完了）
     */
    public Todo(String title, boolean completed) {
        this.title = title;
        this.completed = completed;
    }

    /**
     * ETagのベース文字列を取得する。
     *
     * @return ETagのベース文字列
     */
    public String getETagBase() {
        return getId() + ":" + getUpdatedAt();
    }
}
