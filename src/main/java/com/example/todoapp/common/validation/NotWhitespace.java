package com.example.todoapp.common.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * 空白文字のみの文字列を検証するバリデーションアノテーション
*/
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotWhitespaceValidator.class)
public @interface NotWhitespace {

    /**
     * バリデーションに失敗した場合にエラーメッセージのテンプレートを返す。
     */
    String message() default "{validation.notWhitespace}";

    /**
     * バリデーションのグループを指定する配列を返す。
     */
    Class<?>[] groups() default {};

    /**
     * バリデーションのペイロード情報を指定する配列を返す。
     */
    Class<? extends Payload>[] payload() default {};
}
