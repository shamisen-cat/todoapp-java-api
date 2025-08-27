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
 * To-doの情報を保持するエンティティクラス
 * <p>
 * 監査情報（作成・更新日時）の管理機能を提供する {@link Auditable} を継承
 */
@Entity
@Table(name = "todos")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public final class TodoEntity extends Auditable implements ETagSource {

    /** 自動生成される一意の識別子 */
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
     * 指定されたタイトルの {@link TodoEntity} を生成する。
     *
     * @param title タイトル
     */
    public TodoEntity(String title) {
        this(title, false);
    }

    /**
     * 指定されたタイトルと完了状態の {@link TodoEntity} を生成する。
     *
     * @param title     タイトル
     * @param completed 完了状態（true: 完了、false: 未完了）
     */
    public TodoEntity(String title, boolean completed) {
        this.title = title;
        this.completed = completed;
    }

    /**
     * ETagのベース文字列値を取得する。
     *
     * @return ETagのベース文字列値
     */
    @Override
    public String getETagBase() {
        return getId() + ":" + getUpdatedAt();
    }
}
