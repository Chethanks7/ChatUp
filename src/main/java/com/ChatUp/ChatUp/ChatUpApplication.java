package com.ChatUp.ChatUp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChatUpApplication {

	public static void main(String[] args) {

		SpringApplication.run(ChatUpApplication.class, args);
	}

}
