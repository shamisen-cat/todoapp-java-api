package com.example.todoapp.common.error.factory;

/**
 * エラーレスポンス生成時にエラーメッセージの引数を提供するインタフェース
 * <p>
 * 動的にエラーメッセージを構築する例外クラスに実装する。
 */
public interface ProblemDetailInfo {

    /**
     * エラーメッセージの引数の配列を返す。
     *
     * @return エラーメッセージの引数の配列
     */
    Object[] getMessageArgs();
}
