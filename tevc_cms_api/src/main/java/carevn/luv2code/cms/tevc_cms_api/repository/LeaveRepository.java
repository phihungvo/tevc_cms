package carevn.luv2code.cms.tevc_cms_api.repository;

import carevn.luv2code.cms.tevc_cms_api.entity.Contract;
import carevn.luv2code.cms.tevc_cms_api.entity.Leave;
import carevn.luv2code.cms.tevc_cms_api.enums.LeaveStatus;
import carevn.luv2code.cms.tevc_cms_api.enums.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, UUID> {
    List<Leave> findByEmployeeId(UUID employeeId);
    List<Leave> findByStatus(LeaveStatus status);
    List<Leave> findByStartDateBetween(Date startDate, Date endDate);
    List<Leave> findByLeaveType(LeaveType leaveType);
}
