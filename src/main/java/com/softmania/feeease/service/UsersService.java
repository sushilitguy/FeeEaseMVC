package com.softmania.feeease.service;

import com.softmania.feeease.dto.UserSummary;
import com.softmania.feeease.model.Users;
import com.softmania.feeease.repo.UsersRepo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    @Autowired
    private UsersRepo repo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<Users> getUsers() {
        return repo.findAll();
    }

    public List<Users> getUsersBySchool(int schoolId) {
        return repo.findBySchoolId(schoolId);
    }

    public Users getUserById(int id) {
        return repo.findById(id).orElse(null);
    }

    public Users addUser(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public Users enableUser(int id) {
        Users enabledUser = null;
        Users existingUser = getUserById(id);
        if(existingUser != null) {
            existingUser.setEnabled(true);
            enabledUser = repo.save(existingUser);
        }
        return enabledUser;
    }

    public Users disableUser(int id) {
        Users disabledUser = null;
        Users existingUser = getUserById(id);
        if(existingUser != null) {
            existingUser.setEnabled(false);
            disabledUser = repo.save(existingUser);
        }
        return disabledUser;
    }

    public Users updateUser(Users user) {
        Users updatedUser = null;
        if(repo.existsById(user.getId())) {
            updatedUser = repo.save(user);
        }
        return updatedUser;
    }

    public Users resetPassword(int userId) {
        Users resetUser = null;
        Users existingUser = getUserById(userId);
        if(existingUser != null) {
            existingUser.setPassword(encoder.encode("FeeEase@12345"));
            resetUser = repo.save(existingUser);
        }
        return resetUser;
    }

    public UserSummary getUserSummary(int schoolId) {
        long totalUsers = repo.countBySchoolId(schoolId);
        long activeUsers = repo.countBySchoolIdActive(schoolId);
        long inactiveUsers = totalUsers - activeUsers;

        return new UserSummary(totalUsers, activeUsers, inactiveUsers);
    }
}