package com.oreilly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;

@SpringBootApplication
public class ReactiveOfficersApplication {
    private static Logger log = LoggerFactory.getLogger(ReactiveMongoOperations.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ReactiveOfficersApplication.class, args);
	}
}
