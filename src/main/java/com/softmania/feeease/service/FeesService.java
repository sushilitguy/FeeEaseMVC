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
    @Autowired
    private FeesRepo repo;
    @Autowired
    private StudentsService studentService;
    @Autowired
    private FeeTypeService feeTypeService;
    @Autowired
    private UsersService userService;
    @Autowired
    private AuthenticationManager authManager;

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
        Users currentUser = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserData) {
            currentUser = ((UserData)principal).getUser();
        }
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