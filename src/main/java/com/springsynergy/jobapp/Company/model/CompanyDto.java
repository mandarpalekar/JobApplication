package com.springsynergy.jobapp.Company.model;

import com.springsynergy.jobapp.Job.entity.Job;
import com.springsynergy.jobapp.Job.model.JobDto;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompanyDto {

    private String name;
    private String description;
    private String location;
    private Set<JobDto> jobs;
}