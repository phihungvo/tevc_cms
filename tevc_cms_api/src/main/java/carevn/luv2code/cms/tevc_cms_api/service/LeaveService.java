package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.LeaveDTO;

public interface LeaveService {

    LeaveDTO createLeave(LeaveDTO leaveDTO);

    LeaveDTO updateLeave(UUID id, LeaveDTO leaveDTO);

    void deleteLeave(UUID id);

    LeaveDTO getLeave(UUID id);

    Page<LeaveDTO> getAllLeaves(int page, int size);

    LeaveDTO approveLeave(UUID id, String comments);

    LeaveDTO rejectLeave(UUID id, String comments);

    List<LeaveDTO> getEmployeeLeaves(UUID employeeId);
}
