package com.jobseeker;

import com.jobseeker.model.Job;
import com.jobseeker.repository.JobRepository;
import com.jobseeker.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JobberwockyApplicationTest {
    private JobRepository repository;
    private JobService service;

    @BeforeEach
    void setUp() {
        repository = mock(JobRepository.class);
        service = new JobService(repository);
    }

    @Test
    void getJobs_shouldReturnAllJobs_whenNoFilter() {
        List<Job> jobs = List.of(
                Job.builder()
                        .id("1")
                        .title("Java Dev")
                        .company("Visa")
                        .country("Argentina")
                        .salary(1000)
                        .description("Java AWS Kafka")
                        .build(),
                Job.builder()
                        .id("2")
                        .title("Python Dev")
                        .company("Amex")
                        .country("Spain")
                        .salary(700)
                        .description("Python Django Spark")
                        .build()
        );

        when(repository.findAll()).thenReturn(jobs);

        List<Job> result = service.getJobs();

        assertEquals(2, result.size());
    }

}
