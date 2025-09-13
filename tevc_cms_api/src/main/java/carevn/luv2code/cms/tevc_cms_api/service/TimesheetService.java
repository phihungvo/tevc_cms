package carevn.luv2code.cms.tevc_cms_api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import carevn.luv2code.cms.tevc_cms_api.dto.TimesheetDTO;

public interface TimesheetService {

    TimesheetDTO createTimesheet(TimesheetDTO timesheetDTO);

    TimesheetDTO updateTimesheet(Integer id, TimesheetDTO timesheetDTO);

    void deleteTimesheet(Integer id);

    TimesheetDTO getTimesheet(Integer id);

    Page<TimesheetDTO> getAllTimesheets(int page, int size);

    TimesheetDTO approveTimesheet(Integer id, Integer approverId, String comments);

    TimesheetDTO rejectTimesheet(Integer id, Integer approverId, String comments);

    List<TimesheetDTO> getEmployeeTimesheets(Integer employeeId);

    double calculateOvertimeForEmployee(Integer employeeId, String period);
}
