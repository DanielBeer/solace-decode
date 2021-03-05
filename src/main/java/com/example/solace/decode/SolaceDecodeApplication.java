package com.example.solace.decode;

import com.example.solace.decode.config.SolaceJavaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(SolaceJavaProperties.class)
@SpringBootApplication
public class SolaceDecodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolaceDecodeApplication.class, args);
	}

}
