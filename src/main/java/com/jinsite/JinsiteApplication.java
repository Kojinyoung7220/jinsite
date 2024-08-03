package com.jinsite;

import com.jinsite.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class JinsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(JinsiteApplication.class, args);
	}

}
