package com.springsynergy.jobapp.Company.repository;

import com.springsynergy.jobapp.Company.entity.Company;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

public class CompanyRepositoryCustomImpl implements CompanyRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<Company> findByCompanyName(String companyName) {
        TypedQuery<Company> query = entityManager.createQuery(
                "SELECT c FROM Company c WHERE c.name = :companyName", Company.class);
        query.setParameter("companyName", companyName);
        return query.getResultList().stream().findFirst();
    }
}
