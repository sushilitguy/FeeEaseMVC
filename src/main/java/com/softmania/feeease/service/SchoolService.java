package com.softmania.feeease.service;

import com.softmania.feeease.model.School;
import com.softmania.feeease.repo.SchoolRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolService {
	@Autowired
	private SchoolRepo repo;

	public School registerSchool(School school) {
		return repo.save(school);
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