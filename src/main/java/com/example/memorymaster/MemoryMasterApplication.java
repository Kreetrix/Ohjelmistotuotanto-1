package com.example.memorymaster;

import com.example.memorymaster.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Bean;
import com.example.memorymaster.entity.User;

@SpringBootApplication
@RestController
public class MemoryMasterApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemoryMasterApplication.class, args);
	}
	@GetMapping
	public String helloWorld() {
		return "Hello World";
	}

	@Bean
	public CommandLineRunner demo(UserRepository userRepository) {
		return (args) -> {
			System.out.println("=== Users in DB ===");
			for (User u : userRepository.findAll()) {
				System.out.println(u.getUser_id() + " | " + u.getUsername() + " | " + u.getEmail());
			}
		};
	}
}
