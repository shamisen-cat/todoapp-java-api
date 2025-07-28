package com.example.todoapp.common.exception;

import com.example.todoapp.common.error.ErrorCode;
import com.example.todoapp.common.error.service.ProblemDetailInfo;

/**
 * ETagの検証に失敗した場合にスローされる例外クラス
 */
public final class ETagMismatchException extends ApplicationException
    implements ProblemDetailInfo {

    private static final long serialVersionUID = 1L;

    /**
     * コンテキストの識別情報
     */
    private final String context;

    /**
     * クライアントから受信したIf-Matchヘッダ
     */
    private final String ifMatch;

    /**
     * 検証対象のETag
     */
    private final String expected;

    public ETagMismatchException(String context, String ifMatch, String expected) {
        super(ErrorCode.ETAG_MISMATCH);
        this.context = context;
        this.ifMatch = ifMatch;
        this.expected = expected;
    }

    /**
     * エラーメッセージの引数の配列を返す。
     *
     * @return エラーメッセージの引数の配列
     */
    @Override
    public Object[] getMessageArgs() {
        return new Object[] { getContext(), getIfMatch(), getExpected() };
    }

    public String getContext() {
        return context;
    }

    public String getIfMatch() {
        return ifMatch;
    }

    public String getExpected() {
        return expected;
    }
}
