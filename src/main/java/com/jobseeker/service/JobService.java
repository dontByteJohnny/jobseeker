package com.jobseeker.service;

import com.jobseeker.model.Job;
import com.jobseeker.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepository repository;

    public Job createJob(Job job) {
        job.setId(UUID.randomUUID().toString());
        repository.save(job);
        return job;
    }

    public List<Job> getJobs(String filter) {
        List<Job> allJobs = repository.findAll();
        return allJobs;
    }

}
