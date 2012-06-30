package org.jboss.jbw2012.keynote.utils.annotations;

import java.lang.annotation.ElementType ;
import java.lang.annotation.Retention ;
import java.lang.annotation.RetentionPolicy ;
import java.lang.annotation.Target ;

import javax.enterprise.util.Nonbinding ;
import javax.interceptor.InterceptorBinding ;

@InterceptorBinding
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional
{
    @Nonbinding
    public Class<? extends TransactionalRetryHandler>[] retryHandlers() default {} ;
}
