package com.softmania.feeease.repo;

import com.softmania.feeease.model.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepo extends JpaRepository<School, Integer> {
}
