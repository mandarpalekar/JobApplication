package com.springsynergy.jobapp.Job.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JobDto {

    private String jobTitle;
    private String jobDescription;
    private Long minSalary;
    private Long maxSalary;
    private String location;
    private String companyName;
}
