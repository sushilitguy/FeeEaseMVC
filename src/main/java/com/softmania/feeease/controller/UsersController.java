package com.softmania.feeease.controller;

import com.softmania.feeease.model.School;
import com.softmania.feeease.model.UserData;
import com.softmania.feeease.model.Users;
import com.softmania.feeease.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/fee_ease")
public class UsersController {
    @Autowired
    UsersService service;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String dashboard(Authentication auth, Model model) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        List<Users> users = service.getUsersBySchool(school.getId());
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("users", users);
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        return "users";
    }

    @RequestMapping(value = "/users/add", method = RequestMethod.GET)
    public String addUserForm(Authentication auth, Model model) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        return "userForm";
    }

    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public String addUser(Authentication auth, Model model, @RequestParam("userName") String userName, @RequestParam("password") String password, @RequestParam("role") String role) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        Users user = new Users();
        user.setUserName(userName);
        user.setPassword(password);
        user.setRole(role);
        user.setEnabled(true);
        user.setSchool(school);
        Users addedUser = service.addUser(user);
        if(addedUser != null) {
            model.addAttribute("successMessage", "User Added Successfully");
        } else {
            model.addAttribute("errorMessage","Error while adding user, Please try again");
        }
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        return "userForm";
    }

    @RequestMapping(value = "/users/edit/{userId}", method = RequestMethod.GET)
    public String editUserForm(Authentication auth, Model model, @PathVariable int userId) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("user", service.getUserById(userId));
        return "userForm";
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public String updateUser(Authentication auth, Model model, @RequestParam("userId") int userId, @RequestParam("userName") String userName, @RequestParam("role") String role) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        Users user = service.getUserById(userId);
        if(user != null) {
            user.setUserName(userName);
            user.setRole(role);
            Users updatedUser = service.updateUser(user);
            if (updatedUser != null) {
                model.addAttribute("successMessage", "User "+userName+" updated successfully");
            } else {
                model.addAttribute("errorMessage", "Error while adding user, Please try again");
            }
        } else {
            model.addAttribute("errorMessage", "User not found, Please try with another user");
        }
        List<Users> users = service.getUsersBySchool(school.getId());
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("users", users);
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        return "users";
    }

    @RequestMapping(value = "/users/disable", method = RequestMethod.POST)
    public String disableUser(Authentication auth, Model model, @RequestParam("userId") int userId) {
        Users user = service.disableUser(userId);
        if(user != null) {
            model.addAttribute("successMessage", "User Disabled Successfully");
        } else {
            model.addAttribute("errorMessage","User not found, please try with another user");
        }
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        List<Users> users = service.getUsersBySchool(school.getId());
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("users", users);
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        return "users";
    }

    @RequestMapping(value = "/users/enable", method = RequestMethod.POST)
    public String enableUser(Authentication auth, Model model, @RequestParam("userId") int userId) {
        Users user = service.enableUser(userId);
        if(user != null) {
            model.addAttribute("successMessage", "User Enabled Successfully");
        } else {
            model.addAttribute("errorMessage","User not found, please try with another user");
        }
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        List<Users> users = service.getUsersBySchool(school.getId());
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("users", users);
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        return "users";
    }

    @RequestMapping(value = "/users/resetPassword", method = RequestMethod.POST)
    public String resetPassword(Authentication auth, Model model, @RequestParam("userId") int userId) {
        Users user = service.resetPassword(userId);
        if(user != null) {
            model.addAttribute("successMessage", "Password reset for user " + user.getUserName() + " successful");
        } else {
            model.addAttribute("errorMessage","Failed to reset password, please try with another user");
        }
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        List<Users> users = service.getUsersBySchool(school.getId());
        model.addAttribute("SchoolName",school.getName().toUpperCase());
        model.addAttribute("users", users);
        model.addAttribute("Role", ((UserData)auth.getPrincipal()).getUser().getRole());
        return "users";
    }
}