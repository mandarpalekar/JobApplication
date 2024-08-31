package com.springsynergy.jobapp.Job.service;

import com.springsynergy.jobapp.Job.model.JobDto;

import java.util.Set;
import java.util.UUID;

public interface JobService {

    Set<JobDto> getAllJobs();

    JobDto getJobById(UUID id);

    void createJob(JobDto jobDto);

    JobDto getJobByTitle(String jobTitle);

    boolean deleteJobById(UUID jobId);

    boolean updateJobById(UUID jobId, JobDto jobDto);
}
