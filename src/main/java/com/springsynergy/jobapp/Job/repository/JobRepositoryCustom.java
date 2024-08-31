package com.springsynergy.jobapp.Job.repository;

import com.springsynergy.jobapp.Job.entity.Job;

import java.util.Optional;

public interface JobRepositoryCustom {
    Optional<Job> findByJobTitle(String jobTitle);
}
