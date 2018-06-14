package com.nfjs.reactiveofficers;

import com.nfjs.reactiveofficers.dao.OfficerRepository;
import com.nfjs.reactiveofficers.entities.Officer;
import com.nfjs.reactiveofficers.entities.Rank;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

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
