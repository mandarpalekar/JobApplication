package com.springsynergy.jobapp.util;

import com.springsynergy.jobapp.Company.entity.Company;
import com.springsynergy.jobapp.Company.model.CompanyDto;
import com.springsynergy.jobapp.Job.entity.Job;
import com.springsynergy.jobapp.Job.model.JobDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityDtoMapper {

    private final ModelMapper modelMapper = new ModelMapper();

    public JobDto jobToJobDto(Job job) {

        return modelMapper.map(job, JobDto.class);
    }

    public Job jobDtoToJob(JobDto jobDto) {
        return modelMapper.map(jobDto, Job.class);
    }

    public CompanyDto companyToCompanyDto(Company company) {
        return modelMapper.map(company, CompanyDto.class);
    }

    public Company companyDtoToCompany(CompanyDto companyDto) {
        return modelMapper.map(companyDto, Company.class);
    }
}

