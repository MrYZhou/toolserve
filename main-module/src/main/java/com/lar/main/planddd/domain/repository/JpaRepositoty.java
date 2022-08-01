package com.lar.main.planddd.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaRepositoty extends JpaRepository<PlanEntity, String> {
}
