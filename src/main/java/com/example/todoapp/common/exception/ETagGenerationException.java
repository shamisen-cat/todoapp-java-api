package com.example.todoapp.common.exception;

import com.example.todoapp.common.error.ErrorCode;
import com.example.todoapp.common.error.factory.ProblemDetailInfo;

/**
 * ETagの生成に失敗した場合にスローされる例外クラス
 */
public final class ETagGenerationException extends ApplicationException
    implements ProblemDetailInfo {

    private static final long serialVersionUID = 1L;

    /**
     * コンテキストの識別情報
     */
    private final String context;

    /**
     * 例外理由
     */
    private final String reason;

    public ETagGenerationException(String context, String reason) {
        super(ErrorCode.ETAG_GENERATION_FAILURE);
        this.context = context;
        this.reason = reason;
    }

    /**
     * エラーメッセージの引数の配列を返す。
     *
     * @return エラーメッセージの引数の配列
     */
    @Override
    public Object[] getMessageArgs() {
        return new Object[] { getContext(), getReason() };
    }

    public String getContext() {
        return context;
    }

    public String getReason() {
        return reason;
    }
}
