package com.softmania.feeease.repo;

import com.softmania.feeease.model.Standard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StandardRepo extends JpaRepository<Standard, Integer> {
    @Query("SELECT s FROM Standard s " +
            "WHERE s.school.id = :schoolId")
    List<Standard> findBySchoolId(int schoolId);
}
