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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/fee_ease")
public class StudentController {
    @Autowired
    private StudentsService service;

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public String viewStudents(Authentication auth, Model model) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        List<Students> students = service.getStudentsBySchool(school.getId());
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("students", students);
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

    @RequestMapping(value = "/students/add")
    public String viewAddStudentForm(Authentication auth,Model model) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        return "studentForm";
    }
}
