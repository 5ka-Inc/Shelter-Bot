package ru.kaInc.shelterbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kaInc.shelterbot.model.Report;

import java.util.List;

@Repository
public interface ReportRepo extends JpaRepository<Report, Long> {
    List<Report> getReportsByUserId(Long userId);

    @Query("SELECT r FROM Report r WHERE r.user.username = :username")
    List<Report> findReportsByUsername(@Param("username") String username);

    @Query("SELECT r FROM Report r WHERE r.isReportValid = true AND r.user.username = :username")
    List<Report> findValidReportsByUsername(@Param("username") String username);
}
