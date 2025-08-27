package com.example.todoapp.todo.exception;

import java.util.UUID;

import lombok.Getter;

import com.example.todoapp.common.error.ErrorCode;

/**
 * 指定されたIDのTo-doが存在しない場合の例外クラス
 */
@Getter
public final class TodoEntityNotFoundException extends TodoException {

    private static final long serialVersionUID = 1L;

    /** エラータイトル */
    private static final String ERROR_TITLE = "Todo Not Found";

    /** 検索対象のTo-doのID */
    private final UUID id;

    /**
     * 指定されたIDのTo-doが存在しない場合の例外を生成する。
     *
     * @param id 検索対象のTo-doのID
     */
    public TodoEntityNotFoundException(UUID id) {
        super(ErrorCode.TODO_NOT_FOUND, ERROR_TITLE);
        this.id = id;
    }

    /**
     * 指定されたIDのTo-doが存在しない場合の詳細情報を返す。
     *
     * @return 例外の詳細情報
     */
    @Override
    public String toString() {
        return "%s: %s [id=%s]"
            .formatted(
                getClass().getSimpleName(),
                getErrorTitle(),
                getId()
            );
    }
}
