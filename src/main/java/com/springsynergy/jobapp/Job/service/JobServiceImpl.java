package com.springsynergy.jobapp.Job.service;

import com.springsynergy.jobapp.Company.entity.Company;
import com.springsynergy.jobapp.Company.repository.CompanyRepository;
import com.springsynergy.jobapp.Company.service.CompanyService;
import com.springsynergy.jobapp.Job.entity.Job;
import com.springsynergy.jobapp.exception.JobAlreadyExistsException;
import com.springsynergy.jobapp.Job.model.JobDto;
import com.springsynergy.jobapp.Job.repository.JobRepository;
import com.springsynergy.jobapp.util.EntityDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Set<JobDto> getAllJobs() {
        Set<JobDto> jobDtos = new HashSet<>();
        Set<Job> jobs = new HashSet<>(jobRepository.findAll());
        for (Job job : jobs) {
            jobDtos.add(entityDtoMapper.jobToJobDto(job));
        }
        return jobDtos;
    }

    @Override
    public JobDto getJobById(UUID id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        return jobOptional.map(entityDtoMapper::jobToJobDto).orElse(null);
    }

    @Override
    public void createJob(JobDto jobDto) {
        Optional<Job> existingJob = jobRepository.findByJobTitle(jobDto.getJobTitle());
        if (existingJob.isPresent()) {
            throw new JobAlreadyExistsException("Job with title " + jobDto.getJobTitle() + " already exists.");
        } else {
            Job job = entityDtoMapper.jobDtoToJob(jobDto);
            Optional<Company> companyOptional = companyRepository.findByCompanyName(jobDto.getCompanyName());
            if (companyOptional.isPresent()) {
                job.setCompany(companyOptional.get());
                jobRepository.save(job);
            } else {
                throw new JobAlreadyExistsException("Company with name " + jobDto.getCompanyName() + " does not exist.");
            }
        }
    }

    @Override
    public JobDto getJobByTitle(String jobTitle) {
        Optional<Job> jobOptional = jobRepository.findByJobTitle(jobTitle);
        return jobOptional.map(entityDtoMapper::jobToJobDto).orElse(null);
    }

    @Override
    public boolean deleteJobById(UUID jobId) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isPresent()) {
            jobRepository.delete(jobOptional.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean updateJobById(UUID jobId, JobDto jobDto) {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        String decodedCompanyName = URLDecoder.decode(jobDto.getCompanyName(), StandardCharsets.UTF_8);
        log.info("Company name: {}", decodedCompanyName);
        log.info("Company name length: {}", decodedCompanyName.length());
        Optional<Company> companyOptional = companyRepository.findByCompanyName(decodedCompanyName);
        if(!companyOptional.isPresent()){
            throw new JobAlreadyExistsException("Company with name " + jobDto.getCompanyName() + " does not exist.");
        } else {
            Company company = companyOptional.get();
            if (jobOptional.isPresent()) {
                Job job = jobOptional.get();
                BeanUtils.copyProperties(jobDto, job, "company");
                job.setCompany(company);
                jobRepository.save(job);
                return true;
            }
        }
        return false;
    }
}
