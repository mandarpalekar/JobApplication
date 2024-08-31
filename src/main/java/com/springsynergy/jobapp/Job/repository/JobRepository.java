package com.springsynergy.jobapp.Job.repository;

import com.springsynergy.jobapp.Job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID>, JobRepositoryCustom {

}
