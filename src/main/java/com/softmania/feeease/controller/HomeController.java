package com.softmania.feeease.controller;

import com.softmania.feeease.model.School;
import com.softmania.feeease.model.UserData;
import com.softmania.feeease.service.StudentsService;
import com.softmania.feeease.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
//@RequestMapping("/fee_ease")
public class HomeController {
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UsersService usersService;
    @Autowired
    private StudentsService studentsService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "Home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password. Please try again.");
        }
        return "login";
    }

    @RequestMapping(value = "/fee_ease/register_school", method = RequestMethod.GET)
    public String registerSchool() {
        return "registerSchool";
    }

    @RequestMapping(value = "/fee_ease/dashboard", method = RequestMethod.GET)
    public String dashboard(Authentication auth, Model model) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        model.addAttribute("SchoolName", school.getName().toUpperCase());
        model.addAttribute("UserSummary", usersService.getUserSummary(school.getId()));
        model.addAttribute("StudentSummary", studentsService.getStudentSummary(school.getId()));
        return "dashboard";
    }
}
