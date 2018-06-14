package com.nfjs.reactiveofficers.controllers;

import com.nfjs.reactiveofficers.dao.OfficerRepository;
import com.nfjs.reactiveofficers.entities.Officer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class OfficerController {
    private OfficerRepository repository;

    public OfficerController(OfficerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/officers")
    public Flux<Officer> getAllOfficers() {
        return repository.findAll();
    }

    @GetMapping("/officers/{id}")
    public Mono<Officer> getOfficer(@PathVariable String id) {
        return repository.findById(id);
    }

    @PostMapping("/officers")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Officer> saveOfficer(@RequestBody Officer officer) {
        return repository.save(officer);
    }

    @PutMapping("/officers/{id}")
    public Mono<ResponseEntity<Officer>> updateOfficer(@PathVariable(value = "id") String id,
                                                       @RequestBody Officer officer) {
        return repository.findById(id)
                         .flatMap(existingOfficer -> {
                             existingOfficer.setRank(officer.getRank());
                             existingOfficer.setFirst(officer.getFirst());
                             existingOfficer.setLast(officer.getLast());
                             return repository.save(existingOfficer);
                         })
                         .map(updateOfficer -> new ResponseEntity<>(updateOfficer, HttpStatus.OK))
                         .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/officers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Object>> deleteOfficer(@PathVariable(value = "id") String id) {
        return repository.deleteById(id)
                         .then(Mono.just(ResponseEntity.noContent().build()))
                         .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/officers")
    public Mono<Void> deleteAllOfficers() {
        return repository.deleteAll();
    }
}
