package com.springboot.book.boot.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 메소드 파라미터로 선언된 객체에서만 사용 가능
@Retention(RetentionPolicy.RUNTIME) // 어노테이션을 런타임시까지 사용 가능
public @interface LoginUser {
}
