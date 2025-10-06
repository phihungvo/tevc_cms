package carevn.luv2code.cms.tevc_cms_api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import carevn.luv2code.cms.tevc_cms_api.entity.Attendance;
import carevn.luv2code.cms.tevc_cms_api.enums.AttendanceStatus;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer>, JpaSpecificationExecutor<Attendance> {
    List<Attendance> findByEmployeeId(Integer employeeId);

    Page<Attendance> findAll(Pageable pageable);

    @Query(
            """
			SELECT a
			FROM Attendance a
			JOIN a.employee e
			WHERE (:startDate IS NULL OR a.attendanceDate >= :startDate)
			AND (:endDate IS NULL OR a.attendanceDate <= :endDate)
			AND (:status IS NULL OR a.status = :status)
			AND (:employeeName IS NULL OR LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :employeeName, '%')))
			""")
    List<Attendance> filterAttendances(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") AttendanceStatus status,
            @Param("employeeName") String employeeName);
}
