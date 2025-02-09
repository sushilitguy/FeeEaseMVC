package com.softmania.feeease.repo;

import com.softmania.feeease.dto.Session;
import com.softmania.feeease.model.AcademicSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AcademicSessionRepo extends JpaRepository<AcademicSession, Integer> {
    AcademicSession getAcademicSessionBySessionName(String session);

    @Query("SELECT new com.softmania.feeease.dto.Session(s.sessionName) FROM AcademicSession s " +
            "WHERE s.school.id = :schoolId")
    List<Session> findBySchoolId(int schoolId);
}
