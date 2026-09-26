package com.infera.extension;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/** Bootstraps the Spring Boot backend used by the browser extension. */
public class ExtensionApplication {

	/** Starts the embedded HTTP server and application context. */
	public static void main(String[] args) {
		SpringApplication.run(ExtensionApplication.class, args);
	}

}
