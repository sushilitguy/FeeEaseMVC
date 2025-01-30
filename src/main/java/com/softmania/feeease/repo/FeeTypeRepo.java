package com.softmania.feeease.repo;

import com.softmania.feeease.model.FeeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeTypeRepo extends JpaRepository<FeeType, Integer>{
}
