package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import carevn.luv2code.cms.tevc_cms_api.dto.LeaveDTO;

public interface LeaveService {

    LeaveDTO createLeave(LeaveDTO leaveDTO);

    LeaveDTO updateLeave(Integer id, LeaveDTO leaveDTO);

    void deleteLeave(Integer id);

    LeaveDTO getLeave(Integer id);

    Page<LeaveDTO> getAllLeaves(int page, int size);

    LeaveDTO approveLeave(Integer id, String comments);

    LeaveDTO rejectLeave(Integer id, String comments);

    List<LeaveDTO> getEmployeeLeaves(Integer employeeId);

    Page<LeaveDTO> getLeavesByEmployeeIdPaged(Integer employeeId, Pageable pageable);
}
