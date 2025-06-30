package com.softmania.feeease.controller;

import com.softmania.feeease.model.School;
import com.softmania.feeease.model.Students;
import com.softmania.feeease.model.UserData;
import com.softmania.feeease.service.SchoolManagementService;
import com.softmania.feeease.service.StudentsService;
import com.softmania.feeease.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/fee_ease")
public class StudentController {
    private final StudentsService service;
    private final SchoolManagementService schoolManagementService;

    @Autowired
    public StudentController(StudentsService service, SchoolManagementService schoolManagementService) {
        this.service = service;
        this.schoolManagementService = schoolManagementService;
    }

    @GetMapping("/students")
    public String viewStudents(Authentication auth, Model model) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        model.addAttribute(Const.ATTR_SESSION_LIST, schoolManagementService.getAllSessions(school.getId()));
        model.addAttribute(Const.ATTR_STANDARD_LIST, schoolManagementService.getAllStandards(school.getId()));
        return Const.VIEW_STUDENTS;
    }

    @GetMapping("/students/add")
    public String viewAddStudentForm(Authentication auth, Model model) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        model.addAttribute(Const.ATTR_SESSION_LIST, schoolManagementService.getAllSessions(school.getId()));
        model.addAttribute(Const.ATTR_STANDARD_LIST, schoolManagementService.getAllStandards(school.getId()));
        model.addAttribute(Const.ATTR_SECTION_LIST, schoolManagementService.getAllSections(school.getId()));
        return Const.VIEW_STUDENT_FORM;
    }

    @GetMapping("/students/edit/{studentId}")
    public String viewEditStudentForm(Authentication auth, Model model, @PathVariable int studentId) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        model.addAttribute(Const.ATTR_SESSION_LIST, schoolManagementService.getAllSessions(school.getId()));
        model.addAttribute(Const.ATTR_STANDARD_LIST, schoolManagementService.getAllStandards(school.getId()));
        model.addAttribute(Const.ATTR_SECTION_LIST, schoolManagementService.getAllSections(school.getId()));
        model.addAttribute(Const.ATTR_STUDENT, service.getStudentById(studentId));
        return Const.VIEW_STUDENT_FORM;
    }

    @PostMapping("/students/add")
    public String addStudent(Authentication auth, Model model,
                             @RequestParam("studentName") String studentName,
                             @RequestParam("fatherName") String fatherName,
                             @RequestParam("motherName") String motherName,
                             @RequestParam("dob") LocalDate dob,
                             @RequestParam("contactNo") String contactNo,
                             @RequestParam("sessionSelect") String session,
                             @RequestParam("standardSelect") int standardId,
                             @RequestParam("sectionSelect") int sectionId,
                             @RequestParam("feesAmount") double feesAmount) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();

        Students student = new Students();
        student.setName(studentName);
        student.setFatherName(fatherName);
        student.setMotherName(motherName);
        student.setDob(dob);
        student.setContactNo(contactNo);
        student.setSession(schoolManagementService.getAcademicSessionBySessionName(session));
        student.setStandard(schoolManagementService.getStandardById(standardId));
        student.setSection(schoolManagementService.getSectionById(sectionId).orElse(null));
        student.setFeesAmount(feesAmount);
        student.setSchool(school);

        Students addedStudent = service.addStudent(student);

        if(addedStudent != null) {
            model.addAttribute(Const.ATTR_SUCCESS, "Student Added Successfully");
        } else {
            model.addAttribute(Const.ATTR_ERROR, "Error while adding Student, Please try again");
        }

        model.addAttribute(Const.ATTR_SESSION_LIST, schoolManagementService.getAllSessions(school.getId()));
        model.addAttribute(Const.ATTR_STANDARD_LIST, schoolManagementService.getAllStandards(school.getId()));
        model.addAttribute(Const.ATTR_SECTION_LIST, schoolManagementService.getAllSections(school.getId()));
        return Const.VIEW_STUDENT_FORM;
    }

    @PostMapping("/students/update")
    public String updateStudent(Authentication auth, Model model,
                             @RequestParam("studentId") int studentId,
                             @RequestParam("studentName") String studentName,
                             @RequestParam("fatherName") String fatherName,
                             @RequestParam("motherName") String motherName,
                             @RequestParam("dob") LocalDate dob,
                             @RequestParam("contactNo") String contactNo,
                             @RequestParam("sessionSelect") String session,
                             @RequestParam("standardSelect") int standardId,
                             @RequestParam("sectionSelect") int sectionId,
                             @RequestParam("feesAmount") double feesAmount) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();

        Students student = service.getStudentById(studentId);
        if(student != null) {
            student.setName(studentName);
            student.setFatherName(fatherName);
            student.setMotherName(motherName);
            student.setDob(dob);
            student.setContactNo(contactNo);
            student.setSession(schoolManagementService.getAcademicSessionBySessionName(session));
            student.setStandard(schoolManagementService.getStandardById(standardId));
            student.setSection(schoolManagementService.getSectionById(sectionId).orElse(null));
            student.setFeesAmount(feesAmount);
            student.setSchool(school);

            Students updatedStudent = service.updateStudent(student);

            if (updatedStudent != null) {
                model.addAttribute(Const.ATTR_SUCCESS, "Student Added Successfully");
            } else {
                model.addAttribute(Const.ATTR_ERROR, "Error while adding Student, Please try again");
            }
        } else {
            model.addAttribute(Const.ATTR_ERROR, "Student not found, Please try with another Student");
        }

        model.addAttribute(Const.ATTR_SESSION_LIST, schoolManagementService.getAllSessions(school.getId()));
        model.addAttribute(Const.ATTR_STANDARD_LIST, schoolManagementService.getAllStandards(school.getId()));

        return Const.VIEW_STUDENTS;
    }

    @GetMapping("/students/filter")
    @ResponseBody
    public List<Students> filterStudents(@RequestParam String session, @RequestParam int standardId) {
        return service.getStudentsBySessionIdAndStandard(schoolManagementService.getAcademicSessionBySessionName(session).getId(), standardId);
    }

    @PostMapping("/students/enable")
    @ResponseBody
    public Students enableStudent(Authentication auth, Model model, @RequestParam int studentId) {
        return service.enableStudent(studentId);
    }

    @PostMapping("/students/disable")
    @ResponseBody
    public Students disableStudent(Authentication auth, Model model, @RequestParam int studentId) {
        return service.disableStudent(studentId);
    }
}
