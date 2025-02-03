package com.softmania.feeease.repo;

import com.softmania.feeease.dto.Section;
import com.softmania.feeease.dto.Session;
import com.softmania.feeease.dto.Standard;
import com.softmania.feeease.model.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentsRepo extends JpaRepository<Students, Integer> {
    @Query("SELECT s FROM Students s " +
            "WHERE s.id NOT IN " +
            "(SELECT f.student.id FROM Fees f " +
            "WHERE f.depositMonthYear = :depositMonthYear) AND s.school.id = :schoolId")
    public List<Students> getFeesNotPaidByStudentByMonthYear(int schoolId, String depositMonthYear);

    @Query("SELECT s FROM Students s " +
            "WHERE s.school.id = :schoolId")
    List<Students> findBySchoolId(int schoolId);

    @Query("SELECT COUNT(s) FROM Students s WHERE s.school.id = :schoolId")
    long countBySchoolId(@Param("schoolId") int schoolId);

    @Query("SELECT COUNT(s) FROM Students s WHERE s.school.id = :schoolId AND s.isEnabled = true")
    long countBySchoolIdActive(@Param("schoolId") int schoolId);

    @Query("SELECT DISTINCT new com.softmania.feeease.dto.Session(s.session) from Students s WHERE s.school.id = :schoolId")
    List<Session> getAllSession(@Param("schoolId") int schoolId);

    @Query("SELECT DISTINCT new com.softmania.feeease.dto.Standard(s.standard) from Students s WHERE s.school.id = :schoolId")
    List<Standard> getAllStandards(@Param("schoolId") int schoolId);

    List<Students> findBySessionAndStandard(String session, String standard);

    @Query("SELECT DISTINCT new com.softmania.feeease.dto.Section(s.section) from Students s WHERE s.school.id = :schoolId")
    List<Section> getAllSections(@Param("schoolId") int schoolId);
}
