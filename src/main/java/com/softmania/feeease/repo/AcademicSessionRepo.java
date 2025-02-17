package com.softmania.feeease.repo;

import com.softmania.feeease.dto.Session;
import com.softmania.feeease.model.AcademicSession;
import com.softmania.feeease.model.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AcademicSessionRepo extends JpaRepository<AcademicSession, Integer> {
    AcademicSession getAcademicSessionBySessionName(String session);

    @Query("SELECT new com.softmania.feeease.dto.Session(s.id, s.sessionName, CAST(s.sessionType AS string)) FROM AcademicSession s " +
            "WHERE s.school.id = :schoolId")
    List<Session> findBySchoolId(int schoolId);

    Optional<AcademicSession> findBySessionTypeAndSchoolId(SessionType sessionType, int schoolId);
}
