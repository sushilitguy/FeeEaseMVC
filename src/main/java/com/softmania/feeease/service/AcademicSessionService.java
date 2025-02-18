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
        if(session.getSessionType().equals(SessionType.CURRENT)) {
            AcademicSession currentSession = getAcademicSessionBySessionType(SessionType.CURRENT);
            if(currentSession != null) {
                currentSession.setSessionType(SessionType.CLOSED);
                repository.save(currentSession);
            }
        }
        if(session.getSessionType().equals(SessionType.UPCOMING)) {
            AcademicSession upcomingSession = getAcademicSessionBySessionType(SessionType.UPCOMING);
            if(upcomingSession != null) {
                upcomingSession.setSessionType(SessionType.CLOSED);
                repository.save(upcomingSession);
            }
        }
        return repository.save(session);
    }

    public AcademicSession updateSession(AcademicSession session) {
        if (repository.existsById(session.getId())) {
            if(session.getSessionType().equals(SessionType.CURRENT)) {
                AcademicSession currentSession = getAcademicSessionBySessionType(SessionType.CURRENT);
                if(currentSession != null) {
                    currentSession.setSessionType(SessionType.CLOSED);
                    repository.save(currentSession);
                }
            }
            if(session.getSessionType().equals(SessionType.UPCOMING)) {
                AcademicSession upcomingSession = getAcademicSessionBySessionType(SessionType.UPCOMING);
                if(upcomingSession != null) {
                    upcomingSession.setSessionType(SessionType.CLOSED);
                    repository.save(upcomingSession);
                }
            }
            return repository.save(session);
        }
        return null;
    }

    public AcademicSession getAcademicSessionBySessionType(SessionType sessionType) {
        return repository.getAcademicSessionBySessionType(sessionType);
    }

    public AcademicSession getAcademicSessionBySessionName(String sessionName) {
        return repository.findBySessionName(sessionName);
    }

    public AcademicSession getCurrentAcademicSession(int schoolId) {
        return repository.findBySessionTypeAndSchoolId(SessionType.CURRENT, schoolId).orElse(new AcademicSession());
    }
}
