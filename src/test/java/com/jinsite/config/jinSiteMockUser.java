package com.jinsite.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = jinSiteMockSecurityContext.class)
public @interface jinSiteMockUser {


    String name() default "진사이트";
    String email() default "jin@gmail.com";
    String password() default "";

//    String role() default "ROLE_ADMIN";


}
