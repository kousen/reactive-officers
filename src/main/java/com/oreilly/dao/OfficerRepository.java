package com.oreilly.dao;

import com.oreilly.entities.Officer;
import com.oreilly.entities.Rank;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface OfficerRepository extends ReactiveCrudRepository<Officer, String> {
    Flux<Officer> findByRank(@Param("rank") Rank rank);
    Flux<Officer> findByLast(@Param("last") String last);
}