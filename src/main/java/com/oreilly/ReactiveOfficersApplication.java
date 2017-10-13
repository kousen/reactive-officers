package com.oreilly;

import com.oreilly.controllers.OfficerHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@SpringBootApplication
public class ReactiveOfficersApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(ReactiveOfficersApplication.class, args);
	}

	@Bean
    public RouterFunction<ServerResponse> route(OfficerHandler handler) {
	    return RouterFunctions
                .route(GET("/route/{id}").and(accept(APPLICATION_JSON)), handler::getOfficer)
                .andRoute(GET("/route").and(accept(APPLICATION_JSON)), handler::listOfficers)
                .andRoute(POST("/route").and(contentType(APPLICATION_JSON)), handler::createOfficer);
    }
}
