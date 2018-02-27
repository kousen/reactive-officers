package com.oreilly;

import com.oreilly.dao.OfficerRepository;
import com.oreilly.entities.Officer;
import com.oreilly.entities.Rank;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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
public class OfficerInit implements ApplicationRunner {
    private OfficerRepository dao;

    public OfficerInit(OfficerRepository dao) {
        this.dao = dao;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        dao.deleteAll()
            .thenMany(Flux.just(new Officer(Rank.CAPTAIN, "James", "Kirk"),
                                new Officer(Rank.CAPTAIN, "Jean-Luc", "Picard"),
                                new Officer(Rank.CAPTAIN, "Benjamin", "Sisko"),
                                new Officer(Rank.CAPTAIN, "Kathryn", "Janeway"),
                                new Officer(Rank.CAPTAIN, "Jonathan", "Archer")))
           .flatMap(dao::save)
           .thenMany(dao.findAll())
           .subscribe(System.out::println);
    }
}
