package com.example.HCOData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;


@EnableScheduling
@SpringBootApplication
@EnableJpaAuditing
@Component
public class HcoDataApplication extends SpringBootServletInitializer implements CommandLineRunner {


	@Autowired
	private JdbcTemplate jdbcTemplate;


	public static void main(String[] args) {
		SpringApplication.run(HcoDataApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			jdbcTemplate.execute("SELECT 1");
			System.out.println("Database is connected.");
		} catch (Exception e) {
			System.err.println("Database connection failed: " + e.getMessage());
		}
	}
}
