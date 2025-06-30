package com.softmania.feeease.controller;

import com.softmania.feeease.model.School;
import com.softmania.feeease.model.UserData;
import com.softmania.feeease.model.Users;
import com.softmania.feeease.service.UsersService;
import com.softmania.feeease.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/fee_ease")
public class UsersController {
    private final UsersService service;

    @Autowired
    public UsersController(UsersService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public String dashboard(Authentication auth, Model model) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        List<Users> users = service.getUsersBySchool(school.getId());
        model.addAttribute(Const.ATTR_USERS, users);
        return Const.VIEW_USERS;
    }

    @GetMapping("/users/add")
    public String addUserForm(Authentication auth, Model model) {
        return Const.VIEW_USER_FORM;
    }

    @PostMapping("/users/add")
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
            model.addAttribute(Const.ATTR_SUCCESS, "User Added Successfully");
        } else {
            model.addAttribute(Const.ATTR_ERROR,"Error while adding user, Please try again");
        }
        return Const.VIEW_USER_FORM;
    }

    @GetMapping("/users/edit/{userId}")
    public String editUserForm(Authentication auth, Model model, @PathVariable int userId) {
        model.addAttribute("user", service.getUserById(userId));
        return Const.VIEW_USER_FORM;
    }

    @PostMapping("/users/update")
    public String updateUser(Authentication auth, Model model, @RequestParam("userId") int userId, @RequestParam("userName") String userName, @RequestParam("role") String role) {
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        Users user = service.getUserById(userId);
        if(user != null) {
            user.setUserName(userName);
            user.setRole(role);
            Users updatedUser = service.updateUser(user);
            if (updatedUser != null) {
                model.addAttribute(Const.ATTR_SUCCESS, "User "+userName+" updated successfully");
            } else {
                model.addAttribute(Const.ATTR_ERROR, "Error while adding user, Please try again");
            }
        } else {
            model.addAttribute(Const.ATTR_ERROR, "User not found, Please try with another user");
        }
        List<Users> users = service.getUsersBySchool(school.getId());
        model.addAttribute(Const.ATTR_USERS, users);
        return Const.VIEW_USERS;
    }

    @PostMapping("/users/disable")
    public String disableUser(Authentication auth, Model model, @RequestParam("userId") int userId) {
        Users user = service.disableUser(userId);
        if(user != null) {
            model.addAttribute(Const.ATTR_SUCCESS, "User Disabled Successfully");
        } else {
            model.addAttribute(Const.ATTR_ERROR,"User not found, please try with another user");
        }
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        List<Users> users = service.getUsersBySchool(school.getId());
        model.addAttribute(Const.ATTR_USERS, users);
        return Const.VIEW_USERS;
    }

    @PostMapping("/users/enable")
    public String enableUser(Authentication auth, Model model, @RequestParam("userId") int userId) {
        Users user = service.enableUser(userId);
        if(user != null) {
            model.addAttribute(Const.ATTR_SUCCESS, "User Enabled Successfully");
        } else {
            model.addAttribute(Const.ATTR_ERROR,"User not found, please try with another user");
        }
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        List<Users> users = service.getUsersBySchool(school.getId());
        model.addAttribute(Const.ATTR_USERS, users);
        return Const.VIEW_USERS;
    }

    @PostMapping("/users/resetPassword")
    public String resetPassword(Authentication auth, Model model, @RequestParam("userId") int userId) {
        Users user = service.resetPassword(userId);
        if(user != null) {
            model.addAttribute(Const.ATTR_SUCCESS, "Password reset for user " + user.getUserName() + " successful");
        } else {
            model.addAttribute(Const.ATTR_ERROR,"Failed to reset adminPassword, please try with another user");
        }
        School school = ((UserData)auth.getPrincipal()).getUser().getSchool();
        List<Users> users = service.getUsersBySchool(school.getId());
        model.addAttribute(Const.ATTR_USERS, users);
        return Const.VIEW_USERS;
    }
}