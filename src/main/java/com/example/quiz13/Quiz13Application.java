package com.example.quiz13;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.quiz13", "com.example.dao"})
public class Quiz13Application {

	public static void main(String[] args) {
		SpringApplication.run(Quiz13Application.class, args);
	}

}