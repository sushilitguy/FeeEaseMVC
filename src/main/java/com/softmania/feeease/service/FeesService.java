package com.softmania.feeease.service;

import com.softmania.feeease.model.*;
import com.softmania.feeease.repo.FeesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeesService {
    private final FeesRepo repo;
    private final StudentsService studentService;
    private final FeeTypeService feeTypeService;

    @Autowired
    public FeesService(FeesRepo repo, StudentsService studentService, FeeTypeService feeTypeService, UsersService userService, AuthenticationManager authManager) {
        this.repo = repo;
        this.studentService = studentService;
        this.feeTypeService = feeTypeService;
    }

    public List<Fees> getFeesPaidByYear(int year, int schoolId) {
        return repo.getFeesPaidByYear(year, schoolId);
    }

    public List<Fees> getFeesPaidByMonthYear(int schoolId, String depositMonthYear) {
        return repo.getFeesPaidByMonthYear(schoolId, depositMonthYear);
    }

    public List<Fees> getFeesPaidByStudent(int schoolId, int studentId) throws Exception {
        Students student = studentService.getStudentById(studentId);
        if(student != null) {
            return repo.getFeesPaidByStudent(schoolId, studentId);
        } else {
            throw new Exception("Student not found");
        }
    }

    public List<Students> getFeesNotPaidByStudentByMonthYear(int schoolId, String depositMonthYear) {
        return studentService.getFeesNotPaidByStudentByMonthYear(schoolId, depositMonthYear);
    }

    public Fees addFees(Fees recievedFees) throws Exception {
        Students currentStudent = studentService.getStudentById(recievedFees.getStudent().getId());
        FeeType currentFeeType = feeTypeService.getFeeTypeById(recievedFees.getFeeType().getId());
        UserData principal = (UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users currentUser = principal.getUser();

        if(currentStudent != null && currentFeeType != null && currentUser != null) {
            recievedFees.setFeeType(currentFeeType);
            recievedFees.setStudent(currentStudent);
            recievedFees.setAddedBy(currentUser);
            return repo.save(recievedFees);
        } else {
            throw new Exception("Invalid request");
        }
    }

    public Fees updateFees(Fees fees) {
        Fees updatedFees = null;
        if(repo.existsById(fees.getId())) {
            updatedFees = repo.save(fees);
        }
        return updatedFees;
    }
}