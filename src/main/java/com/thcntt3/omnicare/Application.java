package com.thcntt3.omnicare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/hello")
public class Application {

	@GetMapping
	public String sayHello() {
		return "Hello Omnicare";
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
