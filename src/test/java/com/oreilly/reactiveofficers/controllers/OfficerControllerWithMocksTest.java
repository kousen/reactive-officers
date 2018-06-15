package com.oreilly.reactiveofficers.controllers;

import com.oreilly.reactiveofficers.dao.OfficerRepository;
import com.oreilly.reactiveofficers.entities.Officer;
import com.oreilly.reactiveofficers.entities.Rank;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebFluxTest(OfficerController.class)
public class OfficerControllerWithMocksTest {
    @Autowired
    private WebTestClient client;

    @MockBean
    private OfficerRepository repository;

    @Test
    public void testGetAllOfficers() {
        given(repository.findAll())
                .willReturn(Flux.just(new Officer(Rank.CAPTAIN, "James", "Kirk"),
                                      new Officer(Rank.CAPTAIN, "Jean-Luc", "Picard"),
                                      new Officer(Rank.CAPTAIN, "Benjamin", "Sisko"),
                                      new Officer(Rank.CAPTAIN, "Kathryn", "Janeway"),
                                      new Officer(Rank.CAPTAIN, "Jonathan", "Archer")));
        client.get().uri("/officers")
              .accept(MediaType.APPLICATION_JSON_UTF8)
              .exchange()
              .expectStatus().isOk()
              .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
              .expectBodyList(Officer.class)
              .hasSize(5)
              .consumeWith(System.out::println);
    }

}
