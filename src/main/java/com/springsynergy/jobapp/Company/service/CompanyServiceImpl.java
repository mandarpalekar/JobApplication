package com.springsynergy.jobapp.Company.service;

import com.springsynergy.jobapp.Company.entity.Company;
import com.springsynergy.jobapp.Company.model.CompanyDto;
import com.springsynergy.jobapp.Company.repository.CompanyRepository;
import com.springsynergy.jobapp.Job.entity.Job;
import com.springsynergy.jobapp.Job.model.JobDto;
import com.springsynergy.jobapp.Job.repository.JobRepository;
import com.springsynergy.jobapp.exception.CompanyAlreadyExistsException;
import com.springsynergy.jobapp.util.EntityDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    private final JobRepository jobRepository;

    private final EntityDtoMapper entityDtoMapper;

    @Override
    public Set<CompanyDto> getAllCompanies() {
        Set<CompanyDto> companyDtos = new HashSet<>();
        Set<Company> companies = new HashSet<>(companyRepository.findAll());
        for (Company company : companies) {
            companyDtos.add(entityDtoMapper.companyToCompanyDto(company));
        }
        return companyDtos;
    }

    @Override
    public CompanyDto getCompanyById(UUID companyId) {
        Optional<Company> companyOptional = companyRepository.findById(companyId);
        return companyOptional.map(entityDtoMapper::companyToCompanyDto).orElse(null);
    }

    @Override
    public void createCompany(CompanyDto companyDto) {
        Optional<Company> existingCompany = companyRepository.findByName(companyDto.getName());
        if (existingCompany.isPresent()) {
            throw new CompanyAlreadyExistsException("Company with name " + companyDto.getName() + " already exists.");
        } else {
            Company company = entityDtoMapper.companyDtoToCompany(companyDto);
            log.info("Company: {}", company);
            if(!CollectionUtils.isEmpty(company.getJobs())){
                company.getJobs().forEach(job -> {
                    log.info("Job: {}", job);
                    job.setCompany(company);
                });
            }
            companyRepository.save(company);
        }
    }

    @Override
    public boolean updateCompanyById(String companyId, CompanyDto companyDto) {
        Optional<Company> companyOptional = companyRepository.findById(UUID.fromString(companyId));
        if(companyOptional.isPresent()){
            Company company = companyOptional.get();
            Set<JobDto> jobDtos = companyDto.getJobs();
            if(!CollectionUtils.isEmpty(jobDtos)){
                Set<Job> jobs = new HashSet<>();
                jobDtos.forEach(jobDto -> {
                    Optional<Job> existingJob = jobRepository.findByJobTitle(jobDto.getJobTitle());
                    if(existingJob.isPresent()){
                        Job job = existingJob.get();
                        BeanUtils.copyProperties(jobDto, job);
                        job.setCompany(company);
                        jobs.add(job);
                    }
                });
            }
            BeanUtils.copyProperties(companyDto, company);
            companyRepository.save(company);
            return true;
        }
        return false;
    }

    @Override
    public CompanyDto getCompanyByName(String companyName) {
        log.info("Company name: {}", companyName);
        log.info("Company name length: {}", companyName.length());
        Optional<Company> companyOptional = companyRepository.findByName(companyName);
        return companyOptional.map(entityDtoMapper::companyToCompanyDto).orElse(null);
    }

    @Override
    public boolean deleteCompanyById(String companyId) {
        Optional<Company> companyOptional = companyRepository.findById(UUID.fromString(companyId));
        if (companyOptional.isPresent()) {
            companyRepository.delete(companyOptional.get());
            return true;
        }
        return false;
    }

}
