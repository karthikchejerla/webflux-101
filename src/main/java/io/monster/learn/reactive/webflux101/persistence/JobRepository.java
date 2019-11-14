package io.monster.learn.reactive.webflux101.persistence;

import io.monster.learn.reactive.webflux101.resource.Job;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends ReactiveCrudRepository<Job, String> {
}
