package com.softmania.feeease.repo;

import com.softmania.feeease.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SectionRepo extends JpaRepository<Section, Integer> {
    @Query("SELECT s FROM Section s " +
            "WHERE s.school.id = :schoolId")
    List<Section> findBySchoolId(int schoolId);
}
