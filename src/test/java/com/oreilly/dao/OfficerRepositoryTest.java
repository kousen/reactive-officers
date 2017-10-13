package com.oreilly.dao;

import com.oreilly.entities.Officer;
import com.oreilly.entities.Rank;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

@SuppressWarnings("Duplicates")
@RunWith(SpringRunner.class)
@SpringBootTest
public class OfficerRepositoryTest {
    @Autowired
    private OfficerRepository dao;

    @Autowired
    private ReactiveMongoOperations operations;

    @Before
    public void setUp() {
        operations.collectionExists(Officer.class)
                .flatMap(exists -> exists ? operations.dropCollection(Officer.class) : Mono.just(exists))
                .flatMap(o -> operations.createCollection(Officer.class))
                .then()
                .block();

        dao.saveAll(Flux.just(new Officer(Rank.CAPTAIN, "James", "Kirk"),
                new Officer(Rank.CAPTAIN, "Jean-Luc", "Picard"),
                new Officer(Rank.CAPTAIN, "Benjamin", "Sisko"),
                new Officer(Rank.CAPTAIN, "Kathryn", "Janeway"),
                new Officer(Rank.CAPTAIN, "Jonathan", "Archer")))
                .then()
                .block();
    }

    @Test
    public void testSave() {
        Officer officer = new Officer(Rank.LIEUTENANT, "Nyota", "Uhuru");
        officer = dao.save(officer).block(Duration.ofSeconds(2));
        assertNotNull(Objects.requireNonNull(officer).getId());
        assertEquals(Rank.LIEUTENANT, officer.getRank());
        assertEquals("Nyota", officer.getFirst());
        assertEquals("Uhuru", officer.getLast());
    }

    @Test
    public void findAll() {
        List<String> dbNames = dao.findAll()
                .map(Officer::getLast)
                .collect(Collectors.toList()).block();
        assertThat(dbNames, containsInAnyOrder("Kirk", "Picard", "Sisko", "Janeway", "Archer"));
    }

    @Test
    public void count() {
        assertEquals(5, Objects.requireNonNull(dao.count().block()).intValue());
    }

    @Test
    public void findByRank() {
        List<Officer> officers = dao.findByRank(Rank.CAPTAIN)
                .collectList().block();

        Objects.requireNonNull(officers).forEach(captain ->
                assertEquals(Rank.CAPTAIN, captain.getRank()));
    }

    @Test
    public void findByLast() {
        List<String> lastNames = Arrays.asList("Kirk", "Picard", "Sisko", "Janeway", "Archer");
        lastNames.forEach(lastName -> {
            List<Officer> officers = dao.findByLast(lastName).collectList().block();
            assertEquals(lastName,
                    Objects.requireNonNull(officers).get(0).getLast());
        });
    }
}