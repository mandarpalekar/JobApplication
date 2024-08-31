package com.springsynergy.jobapp.Job.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springsynergy.jobapp.exception.JobAlreadyExistsException;
import com.springsynergy.jobapp.Job.model.JobDto;
import com.springsynergy.jobapp.Job.service.JobService;
import com.springsynergy.jobapp.util.DtoToStringConverter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    private final Set<JobDto> jobs = new HashSet<>();

    private final JobService jobService;

    private final ObjectMapper objectMapper;

    private final DtoToStringConverter dtoToStringConverter;

    @GetMapping("")
    public ResponseEntity<String> getAllJobs() {
        Set<JobDto> jobs = jobService.getAllJobs();
        try {
            if (jobs.isEmpty()) {
                return new ResponseEntity<>("No jobs found", HttpStatus.NOT_FOUND);
            } else {
                String jobsJson = objectMapper.writeValueAsString(jobs);
                return new ResponseEntity<>(jobsJson, HttpStatus.OK);
            }
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error converting jobs to JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/job/id/{jobId}")
    public ResponseEntity<String> getJobById(@PathVariable UUID jobId) {
        JobDto jobDto = jobService.getJobById(jobId);
        return dtoToStringConverter.convertDtoToString(jobDto);
    }

    @GetMapping("/job/title")
    public ResponseEntity<String> getJobByTitle(@RequestParam String jobTitle) {
        if(StringUtils.isBlank(jobTitle)) {
            return new ResponseEntity<>("Job title is required", HttpStatus.BAD_REQUEST);
        }
        JobDto jobDto = jobService.getJobByTitle(jobTitle);
        return dtoToStringConverter.convertDtoToString(jobDto);

    }

    @PostMapping("/job/createJob")
    public ResponseEntity<String> createJob(@RequestBody JobDto job) {
        try {
            jobService.createJob(job);
            return new ResponseEntity<>("Job created successfully", HttpStatus.CREATED);
        } catch (JobAlreadyExistsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/job/deleteJob/id/{jobId}")
    private ResponseEntity<String> deleteJobById(@PathVariable UUID jobId) {
        JobDto jobDto = jobService.getJobById(jobId);
        if (jobDto == null) {
            return new ResponseEntity<>("Job not found", HttpStatus.NOT_FOUND);
        } else {
            boolean isDeleted = jobService.deleteJobById(jobId);
            if(!isDeleted) {
                return new ResponseEntity<>("Error deleting job", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity   <>("Job deleted successfully", HttpStatus.OK);
        }
    }

    @PutMapping("/job/updateJob/id/{jobId}")
    private ResponseEntity<String> updateJobById(@PathVariable UUID jobId, @RequestBody JobDto jobDto) {
        boolean isUpdated = jobService.updateJobById(jobId, jobDto);
        if(!isUpdated) {
            return new ResponseEntity<>("Error updating job", HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>("Job updated successfully", HttpStatus.OK);
        }
    }

}
