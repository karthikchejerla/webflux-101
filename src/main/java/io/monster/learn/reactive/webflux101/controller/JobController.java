package io.monster.learn.reactive.webflux101.controller;

import io.monster.learn.reactive.webflux101.event.JobEvent;
import io.monster.learn.reactive.webflux101.persistence.DatabaseSeeder;
import io.monster.learn.reactive.webflux101.resource.Job;
import io.monster.learn.reactive.webflux101.resource.JobTitle;
import io.monster.learn.reactive.webflux101.service.JobService;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class JobController {

    private static Logger logger = LoggerFactory.getLogger(JobController.class);

    private JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // Get all jobs from database - using reactive data libraries with Mongo as data store
    @GetMapping("/jobs")
    public Publisher<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    // Throwing a custom exception
    @GetMapping("/jobs/{jobId}")
    public Mono<Job> getJobById(@PathVariable String jobId) {
        return jobService.findJobById(jobId)
                         .onErrorMap(RuntimeException::new) // Map to a new business exception
                         .switchIfEmpty(
                             Mono.just(
                                     new Job("1", "Java Developer", "The one who can code in Java", LocalDate.now(), LocalDateTime.now())
                             )
                         );
    }

    // Generating an unbounded stream of data using Flux
    @GetMapping(value = "/jobs/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<JobEvent> getAllJobEvents() {
        return Flux.interval(Duration.ofSeconds(1))
                   .flatMap(x -> jobService.newJobEvent());
    }

    // An example of performing map over a flux/mono
    @PostMapping("/jobs")
    public Mono<Job> postJob(@RequestBody Job jobToPost) {
        return Mono.just(jobToPost)
                   .map(j -> new Job(null, j.getTitle(), j.getDescription(), LocalDate.now(), LocalDateTime.now()))
                   .flatMap(updatedJob -> jobService.createNew(updatedJob));
    }


    // Another example of performing map over a flux/mono
    @GetMapping(value = "/jobs/titles")
    public Flux<JobTitle> getAllJobTitles() {
        return jobService.getAllJobs()
                            .map(Job::getTitle)
                            .map(JobTitle::new);
    }

    // Generate a Flux from an API that returns a Completable Future
    @GetMapping(value = "/jobsByTitle")
    public Flux<Job> getJobByTitle(@RequestParam String title) {
        logger.debug("From Completable Future...");
        return Mono.fromCompletionStage(() -> jobService.getJobsByTitle(title)).flatMapMany(Flux::fromIterable);
    }

    @PostMapping("/jobs/seed")
    public Flux<Job> initialize() {
        return jobService.removeAllJobs()
                         .thenMany(Flux.just(DatabaseSeeder.seedJobsList)
                         .flatMap(job -> jobService.createNew(job)))
                         .thenMany(jobService.getAllJobs());
    }

    // One original call + 3 retries. On failure, Propagate the exception
    @DeleteMapping("/jobs/{jobId}")
    public Mono<Void> deleteJobById(@PathVariable String jobId) {
        return jobService.findJobById(jobId)
                         .flatMap(jobService::delete)
                         .retryBackoff(3, Duration.ofSeconds(2)); // Can use retry, retryBackOff, retryWhen ...
    }

}


