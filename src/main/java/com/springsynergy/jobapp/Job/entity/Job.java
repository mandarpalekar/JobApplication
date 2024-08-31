package com.springsynergy.jobapp.Job.entity;

import com.springsynergy.jobapp.Company.entity.Company;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Job {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false, length = 16)
    private UUID jobId;

    @Column(name = "title" , nullable = false, length = 255, unique = true)
    private String jobTitle;

    @Column(name = "description", length = 255)
    private String jobDescription;

    @Column(name = "minsalary", length = 10)
    private Long minSalary;

    @Column(name = "maxsalary", length = 10)
    private Long maxSalary;

    @Column(name = "location", length = 255)
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "company_name", nullable = false, length = 255)
    private String company_name;

}
