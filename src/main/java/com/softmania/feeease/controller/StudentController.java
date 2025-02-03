package com.softmania.feeease.controller;

import com.softmania.feeease.model.School;
import com.softmania.feeease.model.Students;
import com.softmania.feeease.model.UserData;
import com.softmania.feeease.model.Users;
import com.softmania.feeease.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/fee_ease")
public class StudentController {
    @Autowired
    private StudentsService service;

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public String viewStudents(Authentication auth, Model model) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        model.addAttribute("SessionList", service.getAllSession(school.getId()));
        model.addAttribute("StandardList", service.getAllStandards(school.getId()));
        return "students";
    }

    @RequestMapping(value = "/students/view/{id}")
    public String viewStudentData(Authentication auth, Model model, @PathVariable int id) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        Students student = service.getStudentById(id);
        if(student != null) {
            model.addAttribute("student", student);
            return "viewStudent";
        } else {
            List<Students> students = service.getStudentsBySchool(school.getId());
            model.addAttribute("SchoolName",school.getName().toUpperCase());
            model.addAttribute("students", students);
            model.addAttribute("errorMessage", "Student data with id " + id + " not found");
            model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
            return "students";
        }
    }

    @RequestMapping(value = "/students/add", method = RequestMethod.GET)
    public String viewAddStudentForm(Authentication auth, Model model) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("SessionList", service.getAllSession(school.getId()));
        model.addAttribute("StandardList", service.getAllStandards(school.getId()));
        model.addAttribute("SectionList", service.getAllSections(school.getId()));
        return "studentForm";
    }

    @RequestMapping(value = "/students/edit/{studentId}", method = RequestMethod.GET)
    public String viewEditStudentForm(Authentication auth, Model model, @PathVariable int studentId) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("SessionList", service.getAllSession(school.getId()));
        model.addAttribute("StandardList", service.getAllStandards(school.getId()));
        model.addAttribute("SectionList", service.getAllSections(school.getId()));
        model.addAttribute("student", service.getStudentById(studentId));
        return "studentForm";
    }

    @RequestMapping(value = "/students/add", method = RequestMethod.POST)
    public String addStudent(Authentication auth, Model model,
                             @RequestParam("studentName") String studentName,
                             @RequestParam("fatherName") String fatherName,
                             @RequestParam("motherName") String motherName,
                             @RequestParam("dob") LocalDate dob,
                             @RequestParam("contactNo") String contactNo,
                             @RequestParam("sessionSelect") String session,
                             @RequestParam("standardSelect") String standard,
                             @RequestParam("sectionSelect") String section,
                             @RequestParam("feesAmount") double feesAmount) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();

        Students student = new Students();
        student.setName(studentName);
        student.setFatherName(fatherName);
        student.setMotherName(motherName);
        student.setDob(dob);
        student.setContactNo(contactNo);
        student.setSession(session);
        student.setStandard(standard);
        student.setSection(section);
        student.setFeesAmount(feesAmount);
        student.setSchool(school);

        Students addedStudent = service.addStudent(student);

        if(addedStudent != null) {
            model.addAttribute("successMessage", "Student Added Successfully");
        } else {
            model.addAttribute("errorMessage", "Error while adding Student, Please try again");
        }

        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("SessionList", service.getAllSession(school.getId()));
        model.addAttribute("StandardList", service.getAllStandards(school.getId()));
        model.addAttribute("SectionList", service.getAllSections(school.getId()));
        return "studentForm";
    }

    @RequestMapping(value = "/students/update", method = RequestMethod.POST)
    public String updateStudent(Authentication auth, Model model,
                             @RequestParam("studentId") int studentId,
                             @RequestParam("studentName") String studentName,
                             @RequestParam("fatherName") String fatherName,
                             @RequestParam("motherName") String motherName,
                             @RequestParam("dob") LocalDate dob,
                             @RequestParam("contactNo") String contactNo,
                             @RequestParam("sessionSelect") String session,
                             @RequestParam("standardSelect") String standard,
                             @RequestParam("sectionSelect") String section,
                             @RequestParam("feesAmount") double feesAmount) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();

        Students student = service.getStudentById(studentId);
        if(student != null) {
            student.setName(studentName);
            student.setFatherName(fatherName);
            student.setMotherName(motherName);
            student.setDob(dob);
            student.setContactNo(contactNo);
            student.setSession(session);
            student.setStandard(standard);
            student.setSection(section);
            student.setFeesAmount(feesAmount);
            student.setSchool(school);

            Students updatedStudent = service.updateStudent(student);

            if (updatedStudent != null) {
                model.addAttribute("successMessage", "Student Added Successfully");
            } else {
                model.addAttribute("errorMessage", "Error while adding Student, Please try again");
            }
        } else {
            model.addAttribute("errorMessage", "Student not found, Please try with another Student");
        }

        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        model.addAttribute("SessionList", service.getAllSession(school.getId()));
        model.addAttribute("StandardList", service.getAllStandards(school.getId()));

        return "students";
    }

    @RequestMapping(value = "/students/filter", method = RequestMethod.GET)
    @ResponseBody
    public List<Students> filterStudents(@RequestParam String session, @RequestParam String standard) {
        return service.getStudentsBySessionAndStandard(session, standard);
    }
}
