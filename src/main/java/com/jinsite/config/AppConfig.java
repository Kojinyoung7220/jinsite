package com.jinsite.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "jinsite")
public class AppConfig {

    public String hello;
}
