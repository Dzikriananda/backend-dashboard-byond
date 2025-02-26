package com.dzikriananda.multimatic_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class MultimaticBackendAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultimaticBackendAppApplication.class, args);
	}

}
