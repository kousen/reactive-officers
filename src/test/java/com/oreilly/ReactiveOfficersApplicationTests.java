package com.oreilly;

import com.mongodb.MongoClientOptions;
import com.oreilly.dao.OfficerRepository;
import com.oreilly.entities.Officer;
import com.oreilly.entities.Rank;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReactiveOfficersApplicationTests {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private OfficerRepository repository;

    @Test
    public void testCreateOfficer() {
        Officer officer = new Officer(Rank.LIEUTENANT, "Nyota", "Uhuru");

        webTestClient.post().uri("/officers")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(officer), Officer.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.first").isEqualTo("Nyota")
                .jsonPath("$.last").isEqualTo("Uhuru");
    }

    @Test
    public void testGetAllOfficers() {
        webTestClient.get().uri("/officers")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Officer.class);
    }

    @Test
    public void testGetSingleOfficer() {
        Officer officer = repository.save(new Officer(Rank.ENSIGN, "Pavel", "Chekov")).block();

        webTestClient.get()
                .uri("/officers/{id}", Collections.singletonMap("id", officer.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull());
    }

    @Test
    public void testUpdateOfficer() {
        Officer officer = repository.save(new Officer(Rank.COMMANDER, "NFN", "Data")).block();

        Officer newOfficer = new Officer(Rank.COMMANDER, "NFN", "Lore");

        webTestClient.put()
                .uri("/officers/{id}", Collections.singletonMap("id", officer.getId()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(newOfficer), Officer.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.last").isEqualTo("Lore");
    }

}
