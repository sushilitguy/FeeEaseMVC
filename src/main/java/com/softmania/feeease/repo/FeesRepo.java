package com.softmania.feeease.repo;

import com.softmania.feeease.model.Fees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeesRepo extends JpaRepository<Fees, Integer> {
    @Query("SELECT f FROM Fees f JOIN Students s ON f.student.id = s.id " +
            "WHERE FUNCTION('YEAR', f.depositDateTime) = :year AND s.school.id = :schoolId")
    public List<Fees> getFeesPaidByYear(@Param("year")int year, @Param("schoolId") int schoolId);

    @Query("SELECT f FROM Fees f JOIN Students s ON f.student.id = s.id " +
            "WHERE f.depositMonthYear = :depositMonthYear AND s.school.id = :schoolId")
    public List<Fees> getFeesPaidByMonthYear(@Param("schoolId") int schoolId, @Param("depositMonthYear") String depositMonthYear);

    @Query("SELECT f FROM Fees f JOIN Students s ON f.student.id = s.id " +
            "WHERE f.student.id = :studentId AND s.school.id = :schoolId")
    public List<Fees> getFeesPaidByStudent(@Param("schoolId")int schoolId, @Param("studentId")int studentId);
}
