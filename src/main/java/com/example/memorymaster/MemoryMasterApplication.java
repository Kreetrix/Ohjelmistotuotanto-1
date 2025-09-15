package com.example.memorymaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MemoryMasterApplication {

	public static void main(String[] args) {

		SpringApplication.run(MemoryMasterApplication.class, args);
	}

}
