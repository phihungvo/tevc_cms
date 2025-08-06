package carevn.luv2code.cms.tevc_cms_api.service;

import carevn.luv2code.cms.tevc_cms_api.dto.TimesheetDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface TimesheetService {

    TimesheetDTO createTimesheet(TimesheetDTO timesheetDTO);

    TimesheetDTO updateTimesheet(UUID id, TimesheetDTO timesheetDTO);

    void deleteTimesheet(UUID id);

    TimesheetDTO getTimesheet(UUID id);

    Page<TimesheetDTO> getAllTimesheets(int page, int size);

    TimesheetDTO approveTimesheet(UUID id, UUID approverId, String comments);

    TimesheetDTO rejectTimesheet(UUID id, UUID approverId, String comments);

    List<TimesheetDTO> getEmployeeTimesheets(UUID employeeId);

    double calculateOvertimeForEmployee(UUID employeeId, String period);
}
