package com.softmania.feeease.service;

import com.softmania.feeease.dto.request.RegisterSchoolRequest;
import com.softmania.feeease.model.School;
import com.softmania.feeease.model.Users;
import com.softmania.feeease.repo.SchoolRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolService {
	private final SchoolRepo repo;
	private final UsersService usersService;

	@Autowired
	public SchoolService(SchoolRepo repo, UsersService usersService) {
		this.repo = repo;
		this.usersService = usersService;
	}

	@Transactional
	public boolean registerSchool(RegisterSchoolRequest registerSchoolRequest) {
		School school = new School();
		school.setName(registerSchoolRequest.schoolName());
		school.setAddress(registerSchoolRequest.address());
		school.setContactNo(registerSchoolRequest.contactNo());
		school.setEmail(registerSchoolRequest.email());

		School savedSchool = repo.save(school);

		Users user = new Users();
		user.setSchool(savedSchool);
		user.setUserName(registerSchoolRequest.adminUserName());
		user.setPassword(registerSchoolRequest.adminPassword());
		user.setRole("Admin");
		user.setEnabled(true);
		usersService.addUser(user);
		return true;
	}

	public School updateSchool(School school) {
		if(repo.existsById(school.getId())) {
			return repo.save(school);
		} else {
			return null;
		}
	}

	public School getSchoolBySchoolId(int id) {
		return repo.findById(id).orElse(null);
	}

	public List<School> getSchools() {
		return repo.findAll();
	}
}