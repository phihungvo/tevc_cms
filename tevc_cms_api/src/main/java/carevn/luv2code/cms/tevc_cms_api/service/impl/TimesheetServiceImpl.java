package carevn.luv2code.cms.tevc_cms_api.service.impl;

import static carevn.luv2code.cms.tevc_cms_api.constants.AppConstants.PERIOD_PATTERN;
import static carevn.luv2code.cms.tevc_cms_api.constants.TimesheetConstants.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import carevn.luv2code.cms.tevc_cms_api.dto.TimesheetDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.entity.Project;
import carevn.luv2code.cms.tevc_cms_api.entity.Timesheet;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.TimesheetMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.ProjectRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.TimesheetRepository;
import carevn.luv2code.cms.tevc_cms_api.service.TimesheetService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimesheetServiceImpl implements TimesheetService {
    private final TimesheetRepository timesheetRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final TimesheetMapper timesheetMapper;

    @Override
    @Transactional
    public TimesheetDTO createTimesheet(TimesheetDTO timesheetDTO) {
        Timesheet timesheet = timesheetMapper.toEntity(timesheetDTO);
        timesheet.setStatus("PENDING");
        timesheet.setSubmissionDate(new Date());

        Employee employee = employeeRepository
                .findById(timesheetDTO.getEmployeeId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        timesheet.setEmployee(employee);

        if (timesheetDTO.getProjectId() != null) {
            Project project = projectRepository
                    .findById(timesheetDTO.getProjectId())
                    .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
            timesheet.setProject(project);
        }

        return timesheetMapper.toDTO(timesheetRepository.save(timesheet));
    }

    @Override
    @Transactional
    public TimesheetDTO updateTimesheet(Integer id, TimesheetDTO timesheetDTO) {
        Timesheet timesheet =
                timesheetRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TIMESHEET_NOT_FOUND));

        if (!"PENDING".equals(timesheet.getStatus())) {
            throw new AppException(ErrorCode.TIMESHEET_ALREADY_PROCESSED);
        }

        timesheetMapper.updateFromDto(timesheetDTO, timesheet);
        return timesheetMapper.toDTO(timesheetRepository.save(timesheet));
    }

    @Override
    public TimesheetDTO getTimesheet(Integer id) {
        return timesheetMapper.toDTO(
                timesheetRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TIMESHEET_NOT_FOUND)));
    }

    @Override
    public Page<TimesheetDTO> getAllTimesheets(int page, int size) {
        return timesheetRepository.findAll(PageRequest.of(page, size)).map(timesheetMapper::toDTO);
    }

    @Override
    @Transactional
    public TimesheetDTO approveTimesheet(Integer id, Integer approverId, String comments) {
        Timesheet timesheet =
                timesheetRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TIMESHEET_NOT_FOUND));

        if (!"PENDING".equals(timesheet.getStatus())) {
            throw new AppException(ErrorCode.TIMESHEET_ALREADY_PROCESSED);
        }

        Employee approver = employeeRepository
                .findById(approverId)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        timesheet.setApprover(approver);
        timesheet.setStatus("APPROVED");
        timesheet.setComments(comments);
        timesheet.setApprovalDate(new Date());

        return timesheetMapper.toDTO(timesheetRepository.save(timesheet));
    }

    @Override
    @Transactional
    public TimesheetDTO rejectTimesheet(Integer id, Integer approverId, String comments) {
        Timesheet timesheet =
                timesheetRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TIMESHEET_NOT_FOUND));

        Employee approver = employeeRepository
                .findById(approverId)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        timesheet.setApprover(approver);
        timesheet.setStatus("REJECTED");
        timesheet.setComments(comments);
        timesheet.setApprovalDate(new Date());

        return timesheetMapper.toDTO(timesheetRepository.save(timesheet));
    }

    @Override
    public List<TimesheetDTO> getEmployeeTimesheets(Integer employeeId) {
        return timesheetRepository.findByEmployeeId(employeeId).stream()
                .map(timesheetMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public double calculateOvertimeForEmployee(Integer employeeId, String period) {
        if (period == null || !PERIOD_PATTERN.matcher(period).matches()) {
            throw new AppException(ErrorCode.INVALID_PAYROLL_PERIOD);
        }

        // Phân tích period thành year và month
        String[] periodParts = period.split("-");
        int year = Integer.parseInt(periodParts[0]);
        int month = Integer.parseInt(periodParts[1]);
        if (month < 1 || month > 12) {
            throw new AppException(ErrorCode.INVALID_PAYROLL_PERIOD);
        }

        // Tìm nhân viên và kiểm tra position
        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        if (employee.getPosition() == null || employee.getPosition().getBaseSalary() == null) {
            throw new AppException(ErrorCode.POSITION_NOT_FOUND);
        }
        double baseSalary = employee.getPosition().getBaseSalary();
        double hourlyRate = baseSalary / STANDARD_HOURS_PER_MONTH;
        double overtimeHourlyRate = hourlyRate * OVERTIME_RATE;

        List<Timesheet> timesheets = timesheetRepository.findApprovedByEmployeeIdAndPeriod(employeeId, year, month);

        // Tính tổng giờ làm thêm
        double totalOvertimeHours = timesheets.stream()
                .filter(t -> t.getHoursWorked() != null && t.getDate() != null)
                .mapToDouble(t -> {
                    double overtimeHours = Math.max(0, t.getHoursWorked() - STANDARD_HOURS_PER_DAY);
                    return overtimeHours;
                })
                .sum();

        // Tính tiền làm thêm
        double overtimePay = totalOvertimeHours * overtimeHourlyRate;

        return overtimePay;
    }

    @Override
    @Transactional
    public void deleteTimesheet(Integer id) {
        Timesheet timesheet =
                timesheetRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TIMESHEET_NOT_FOUND));

        if (!"PENDING".equals(timesheet.getStatus())) {
            throw new AppException(ErrorCode.TIMESHEET_ALREADY_PROCESSED);
        }

        timesheetRepository.delete(timesheet);
    }
}
