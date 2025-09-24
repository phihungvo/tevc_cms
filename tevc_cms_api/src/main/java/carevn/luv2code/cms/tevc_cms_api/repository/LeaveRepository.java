package carevn.luv2code.cms.tevc_cms_api.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Leave;
import carevn.luv2code.cms.tevc_cms_api.enums.LeaveStatus;
import carevn.luv2code.cms.tevc_cms_api.enums.LeaveType;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Integer> {
    List<Leave> findByEmployeeId(Integer employeeId);

    List<Leave> findByLeaveStatus(LeaveStatus status);

    List<Leave> findByStartDateBetween(Date startDate, Date endDate);

    List<Leave> findByLeaveType(LeaveType leaveType);

    Page<Leave> findByEmployeeId(Integer employeeId, Pageable pageable);
}
