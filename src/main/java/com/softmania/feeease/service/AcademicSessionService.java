package com.softmania.feeease.service;

import com.softmania.feeease.dto.Session;
import com.softmania.feeease.model.AcademicSession;
import com.softmania.feeease.model.SessionType;
import com.softmania.feeease.repo.AcademicSessionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AcademicSessionService {
    @Autowired
    private AcademicSessionRepo repository;

    public List<Session> getAllSessions(int schoolId) {
        return repository.findBySchoolId(schoolId);
    }

    public Optional<AcademicSession> getSessionById(int id) {
        return repository.findById(id);
    }

    public AcademicSession createSession(AcademicSession session) {
        return repository.save(session);
    }

    public AcademicSession updateSession(int id, AcademicSession session) {
        if (repository.existsById(id)) {
            session.setId(id);
            return repository.save(session);
        }
        return null;
    }

    public void deleteSession(int id) {
        repository.deleteById(id);
    }

    public AcademicSession getAcademicSessionBySession(String session) {
        return repository.getAcademicSessionBySessionName(session);
    }

    public AcademicSession getCurrentAcademicSession(int schoolId) {
        return repository.findBySessionTypeAndSchoolId(SessionType.CURRENT, schoolId).orElse(new AcademicSession());
    }
}
