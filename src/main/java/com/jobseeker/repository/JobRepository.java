package com.jobseeker.repository;

import com.jobseeker.model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class JobRepository {

    private static final String JOB_KEY_PREFIX = "job:";
    private static final String JOB_INDEX_KEY = "jobs:index";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void save(Job job) {
        String jobKey = getJobKey(job.getId());
        redisTemplate.opsForHash().put(jobKey, "job", job);
        redisTemplate.opsForSet().add(JOB_INDEX_KEY, job.getId());
    }

    public Optional<Job> findById(String id) {
        return Optional.ofNullable((Job) redisTemplate.opsForHash().get(getJobKey(id), "job"));
    }

    public List<Job> findAll() {
        Set<Object> ids = redisTemplate.opsForSet().members(JOB_INDEX_KEY);
        if (ids == null || ids.isEmpty()) return Collections.emptyList();

        return ids.stream()
                .map(Object::toString)
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private String getJobKey(String id) {
        return JOB_KEY_PREFIX + id;
    }
}
