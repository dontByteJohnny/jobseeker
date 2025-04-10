package com.jobseeker.controller;

import com.jobseeker.model.Job;
import com.jobseeker.service.ExternalJobService;
import com.jobseeker.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;
    @Autowired
    private ExternalJobService externalJobService;

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        Job created = jobService.createJob(job);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs(
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<Job> localJobs = jobService.getJobs();
        List<Job> externalJobs = externalJobService.fetchExternalJobs();

        List<Job> allJobs = new ArrayList<>();
        allJobs.addAll(localJobs);
        allJobs.addAll(externalJobs);

        if (filter != null && !filter.isBlank()) {
            String lower = filter.toLowerCase();
            allJobs = allJobs.stream()
                    .filter(job ->
                            job.getTitle().toLowerCase().contains(lower) ||
                                    job.getCountry().toLowerCase().contains(lower) ||
                                    job.getSkills().stream().anyMatch(skill -> skill.toLowerCase().contains(lower))
                    )
                    .collect(Collectors.toList());
        }

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, allJobs.size());

        if (fromIndex > allJobs.size()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<Job> paginated = allJobs.subList(fromIndex, toIndex);

        return ResponseEntity.ok(paginated);
    }

}