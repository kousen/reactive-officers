package com.oreilly;

import com.oreilly.dao.OfficerRepository;
import com.oreilly.entities.Officer;
import com.oreilly.entities.Rank;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Component
public class OfficerInit implements CommandLineRunner {
    private ReactiveMongoOperations operations;
    private OfficerRepository dao;

    public OfficerInit(ReactiveMongoOperations operations, OfficerRepository dao) {
        this.operations = operations;
        this.dao = dao;
    }

    @Override
    public void run(String... args) throws Exception {
        operations.collectionExists(Officer.class)
                .flatMap(exists -> exists ? operations.dropCollection(Officer.class) : Mono.just(exists))
                .flatMap(o -> operations.createCollection(Officer.class,
                        CollectionOptions.empty().size(1024 * 1024).maxDocuments(100).capped()))
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
}
