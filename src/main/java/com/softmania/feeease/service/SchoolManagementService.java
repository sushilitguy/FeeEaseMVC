package com.softmania.feeease.service;

import com.softmania.feeease.dto.Session;
import com.softmania.feeease.model.AcademicSession;
import com.softmania.feeease.model.Section;
import com.softmania.feeease.model.SessionType;
import com.softmania.feeease.model.Standard;
import com.softmania.feeease.repo.AcademicSessionRepo;
import com.softmania.feeease.repo.SectionRepo;
import com.softmania.feeease.repo.StandardRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolManagementService {
    @Autowired
    private AcademicSessionRepo sessionRepository;
    @Autowired
    private StandardRepo standardRepository;
    @Autowired
    private SectionRepo sectionRepo;

    public List<Session> getAllSessions(int schoolId) {
        return sessionRepository.findBySchoolId(schoolId);
    }

    public Optional<AcademicSession> getSessionById(int id) {
        return sessionRepository.findById(id);
    }

    public AcademicSession createSession(AcademicSession session) {
        if(session.getSessionType().equals(SessionType.CURRENT)) {
            AcademicSession currentSession = getAcademicSessionBySessionType(SessionType.CURRENT);
            if(currentSession != null) {
                currentSession.setSessionType(SessionType.CLOSED);
                sessionRepository.save(currentSession);
            }
        }
        if(session.getSessionType().equals(SessionType.UPCOMING)) {
            AcademicSession upcomingSession = getAcademicSessionBySessionType(SessionType.UPCOMING);
            if(upcomingSession != null) {
                upcomingSession.setSessionType(SessionType.CLOSED);
                sessionRepository.save(upcomingSession);
            }
        }
        return sessionRepository.save(session);
    }

    public AcademicSession updateSession(AcademicSession session) {
        if (sessionRepository.existsById(session.getId())) {
            if(session.getSessionType().equals(SessionType.CURRENT)) {
                AcademicSession currentSession = getAcademicSessionBySessionType(SessionType.CURRENT);
                if(currentSession != null) {
                    currentSession.setSessionType(SessionType.CLOSED);
                    sessionRepository.save(currentSession);
                }
            }
            if(session.getSessionType().equals(SessionType.UPCOMING)) {
                AcademicSession upcomingSession = getAcademicSessionBySessionType(SessionType.UPCOMING);
                if(upcomingSession != null) {
                    upcomingSession.setSessionType(SessionType.CLOSED);
                    sessionRepository.save(upcomingSession);
                }
            }
            return sessionRepository.save(session);
        }
        return null;
    }

    public AcademicSession getAcademicSessionBySessionType(SessionType sessionType) {
        return sessionRepository.getAcademicSessionBySessionType(sessionType);
    }

    public AcademicSession getAcademicSessionBySessionName(String sessionName) {
        return sessionRepository.findBySessionName(sessionName);
    }

    public AcademicSession getCurrentAcademicSession(int schoolId) {
        return sessionRepository.findBySessionTypeAndSchoolId(SessionType.CURRENT, schoolId).orElse(new AcademicSession());
    }

    public List<Standard> getAllStandards(int schoolId) {
        return standardRepository.findBySchoolId(schoolId);
    }

    public Standard getStandardById(int standardId) {
        return standardRepository.findById(standardId).orElse(null);
    }

    public Standard addStandard(Standard standard) {
        return standardRepository.save(standard);
    }

    public Standard updateStandard(Standard existingStandard) {
        return standardRepository.save(existingStandard);
    }

    public List<Section> getAllSections(int schoolId) {
        return sectionRepo.findBySchoolId(schoolId);
    }

    public Optional<Section> getSectionById(int sectionId) {
        return sectionRepo.findById(sectionId);
    }

    public Section addSection(Section section) {
        return sectionRepo.save(section);
    }

    public Section updateSection(Section existingSection) {
        return sectionRepo.save(existingSection);
    }
}
