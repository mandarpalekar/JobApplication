package com.springsynergy.jobapp.Job.repository;

import com.springsynergy.jobapp.Job.entity.Job;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

public class JobRespositoryImpl implements JobRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Job> findByJobTitle(String jobTitle) {
        TypedQuery<Job> query = entityManager.createQuery(
                "SELECT j FROM Job j WHERE j.jobTitle = :jobTitle", Job.class);
        query.setParameter("jobTitle", jobTitle);
        return query.getResultList().stream().findFirst();
    }

}
