package com.springsynergy.jobapp.Company.repository;

import com.springsynergy.jobapp.Company.entity.Company;

import java.util.Optional;

public interface CompanyRepositoryCustom {

    Optional<Company> findByCompanyName(String companyName);
}
