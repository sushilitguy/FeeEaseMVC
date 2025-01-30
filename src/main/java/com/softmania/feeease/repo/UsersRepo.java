package com.softmania.feeease.repo;

import com.softmania.feeease.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepo extends JpaRepository<Users, Integer> {
    Users findByUserName(String username);

    @Query("SELECT u FROM Users u " +
            "WHERE u.school.id = :schoolId")
    List<Users> findBySchoolId(int schoolId);

    @Query("SELECT COUNT(u) FROM Users u WHERE u.school.id = :schoolId")
    long countBySchoolId(@Param("schoolId") int schoolId);

    @Query("SELECT COUNT(u) FROM Users u WHERE u.school.id = :schoolId AND u.isEnabled = true")
    long countBySchoolIdActive(@Param("schoolId") int schoolId);
}
