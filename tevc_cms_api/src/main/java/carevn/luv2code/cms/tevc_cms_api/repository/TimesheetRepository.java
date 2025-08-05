package carevn.luv2code.cms.tevc_cms_api.repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, UUID> {
    List<Timesheet> findByEmployeeId(UUID employeeId);

    List<Timesheet> findByProjectId(UUID projectId);

    List<Timesheet> findByDateBetween(Date startDate, Date endDate);

    List<Timesheet> findByStatus(String status);

    @Query("SELECT t FROM Timesheet t WHERE t.employee.id = :employeeId " +
            "AND t.status = 'APPROVED' " +
            "AND YEAR(t.date) = :year AND MONTH(t.date) = :month")
    List<Timesheet> findApprovedByEmployeeIdAndPeriod(UUID employeeId, int year, int month);
}
