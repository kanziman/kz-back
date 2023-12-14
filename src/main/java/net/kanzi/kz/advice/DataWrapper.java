package net.kanzi.kz.advice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * see also {@link CustomResponseBodyAdvice}
 * If it is annotated, put response object into 'data' field
 * eg {'data':'response'}
 * @Author zorba
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataWrapper {
}
