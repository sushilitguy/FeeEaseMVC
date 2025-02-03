package com.softmania.feeease.service;

import com.softmania.feeease.dto.Section;
import com.softmania.feeease.dto.Session;
import com.softmania.feeease.dto.Standard;
import com.softmania.feeease.dto.StudentSummary;
import com.softmania.feeease.model.Students;
import com.softmania.feeease.model.Users;
import com.softmania.feeease.repo.StudentsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentsService {
    @Autowired
    private StudentsRepo repo;

    public List<Students> getStudents() {
        return repo.findAll();
    }

    public List<Students> getStudentsBySchool(int schoolId) {
        return repo.findBySchoolId(schoolId);
    }

    public Students getStudentById(int id) {
        return repo.findById(id).orElse(null);
    }

    public Students addStudent(Students student) {
        return repo.save(student);
    }

    public Students updateStudent(Students student) {
        Students updatedStudent = null;
        if(repo.existsById(student.getId())) {
            updatedStudent = repo.save(student);
        }
        return updatedStudent;
    }

    public Students enableStudent(int id) {
        Students enabledStudent = null;
        Students existingStudent = getStudentById(id);
        if(existingStudent != null) {
            existingStudent.setEnabled(true);
            enabledStudent = repo.save(existingStudent);
        }
        return enabledStudent;
    }

    public Students disableStudent(int id) {
        Students disabledStudent = null;
        Students existingStudent = getStudentById(id);
        if(existingStudent != null) {
            existingStudent.setEnabled(false);
            disabledStudent = repo.save(existingStudent);
        }
        return disabledStudent;
    }

    public List<Students> getFeesNotPaidByStudentByMonthYear(int schoolId, String depositMonthYear) {
        return repo.getFeesNotPaidByStudentByMonthYear(schoolId, depositMonthYear);
    }

    public StudentSummary getStudentSummary(int schoolId) {
        long totalStudents = repo.countBySchoolId(schoolId);
        long activeStudents = repo.countBySchoolIdActive(schoolId);
        long inactiveStudents = totalStudents - activeStudents;

        return new StudentSummary(totalStudents, activeStudents, inactiveStudents);
    }

    public List<Session> getAllSession(int schoolId) {
        return repo.getAllSession(schoolId);
    }

    public List<Standard> getAllStandards(int schoolId) {
        return repo.getAllStandards(schoolId);
    }

    public List<Students> getStudentsBySessionAndStandard(String session, String standard) {
        return repo.findBySessionAndStandard(session, standard);
    }

    public List<Section> getAllSections(int schoolId) {
        return repo.getAllSections(schoolId);
    }
}