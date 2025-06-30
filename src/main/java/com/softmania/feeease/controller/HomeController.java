package com.softmania.feeease.controller;

import com.softmania.feeease.dto.request.RegisterSchoolRequest;
import com.softmania.feeease.model.School;
import com.softmania.feeease.model.UserData;
import com.softmania.feeease.service.SchoolManagementService;
import com.softmania.feeease.service.SchoolService;
import com.softmania.feeease.service.StudentsService;
import com.softmania.feeease.service.UsersService;
import com.softmania.feeease.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private final SchoolService schoolService;
    private final UsersService usersService;
    private final StudentsService studentsService;
    private final SchoolManagementService schoolManagementService;

    @Autowired
    public HomeController(SchoolService schoolService, UsersService usersService, StudentsService studentsService, SchoolManagementService schoolManagementService) {
        this.schoolService = schoolService;
        this.usersService = usersService;
        this.studentsService = studentsService;
        this.schoolManagementService = schoolManagementService;
    }

    @GetMapping("/")
    public String home(Authentication auth) {
        if(auth != null && auth.isAuthenticated()) {
            return Const.VIEW_REDIRECT_DASHBOARD;
        }
        return Const.VIEW_HOME;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model, Authentication auth) {
        if(auth != null && auth.isAuthenticated()) {
            return Const.VIEW_REDIRECT_DASHBOARD;
        }
        if (error != null) {
            model.addAttribute(Const.ATTR_ERROR, "Invalid Username or Password. Please try again.");
        }
        return Const.VIEW_LOGIN;
    }

    @GetMapping("/fee_ease/register_school")
    public String registerSchool(Authentication auth) {
        if(auth != null && auth.isAuthenticated()) {
            return Const.VIEW_REDIRECT_DASHBOARD;
        }
        return Const.VIEW_REGISTER;
    }

    @PostMapping("/fee_ease/register_school")
    public String registerSchool(Authentication auth, Model model, @ModelAttribute RegisterSchoolRequest registerSchoolRequest){
        if(auth != null && auth.isAuthenticated()) {
            return Const.VIEW_REDIRECT_DASHBOARD;
        }
        boolean isRegistered = schoolService.registerSchool(registerSchoolRequest);
        if(isRegistered) {
            model.addAttribute(Const.ATTR_SUCCESS, "School Registered Successfully");
        } else {
            model.addAttribute(Const.ATTR_ERROR, "Some issue while registering school, please try after sometime");
        }
        return Const.VIEW_REGISTER;
    }

    @GetMapping("/fee_ease/dashboard")
    public String dashboard(Authentication auth, Model model) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        model.addAttribute(Const.ATTR_USER_SUMMARY, usersService.getUserSummary(school.getId()));
        model.addAttribute(Const.ATTR_STUDENT_SUMMARY, studentsService.getStudentSummary(school.getId()));
        model.addAttribute(Const.ATTR_CURR_SESSION, schoolManagementService.getCurrentAcademicSession(school.getId()).getSessionName());
        return Const.VIEW_DASHBOARD;
    }
}
