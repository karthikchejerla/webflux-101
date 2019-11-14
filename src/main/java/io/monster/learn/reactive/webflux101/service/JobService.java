package io.monster.learn.reactive.webflux101.service;

import io.monster.learn.reactive.webflux101.event.EventType;
import io.monster.learn.reactive.webflux101.event.JobEvent;
import io.monster.learn.reactive.webflux101.persistence.JobRepository;
import io.monster.learn.reactive.webflux101.resource.Job;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class JobService {

    private static Logger logger = LoggerFactory.getLogger(JobService.class);

    private JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public CompletableFuture<List<Job>> getJobsByTitle(String title) {
        List<Job> jobs = Arrays.asList(
          new Job(UUID.randomUUID().toString(), title, "a job description", LocalDate.now(), LocalDateTime.now()),
          new Job(UUID.randomUUID().toString(), title, "another job", LocalDate.now(), LocalDateTime.now()),
          new Job(UUID.randomUUID().toString(), title, "yet another job", LocalDate.now(), LocalDateTime.now())
        );
        return CompletableFuture.completedFuture(jobs);
    }

    public Mono<Job> createNew(Job jobToCreate) {
        return jobRepository.save(jobToCreate);
    }

    public Flux<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Mono<Job> findJobById(String jobId) {
        System.out.println("Looking up Job Id :" + jobId);
        if ("42".equals(jobId)) {
            throw new IllegalStateException("Random Error");
        }
        return jobRepository.findById(jobId);
    }

    public Publisher<JobEvent> newJobEvent() {
        return Flux.just(new JobEvent(EventType.CREATED, LocalDateTime.now()));
    }

    public Mono<Void> removeAllJobs() {
        return jobRepository.deleteAll();
    }

    public Mono<Void> delete(Job jobToDelete) {
        logger.debug("Attempting to delete job " + jobToDelete.getId() + " @ " + Instant.now());
        if (ThreadLocalRandom.current().nextBoolean() == Boolean.FALSE) {
            logger.debug("Delete attempt failed for job " + jobToDelete.getId());
            return Mono.error(() -> new RuntimeException("Exception thrown to test retry"));
        }
        logger.debug("Delete attempt successful for job " + jobToDelete.getId());
        return jobRepository.delete(jobToDelete);
    }
}
