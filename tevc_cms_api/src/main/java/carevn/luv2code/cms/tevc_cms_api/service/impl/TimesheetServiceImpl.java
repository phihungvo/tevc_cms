package carevn.luv2code.cms.tevc_cms_api.service.impl;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        
        Employee employee = employeeRepository.findById(timesheetDTO.getEmployeeId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        timesheet.setEmployee(employee);

        if (timesheetDTO.getProjectId() != null) {
            Project project = projectRepository.findById(timesheetDTO.getProjectId())
                    .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
            timesheet.setProject(project);
        }

        return timesheetMapper.toDTO(timesheetRepository.save(timesheet));
    }

    @Override
    @Transactional
    public TimesheetDTO updateTimesheet(UUID id, TimesheetDTO timesheetDTO) {
        Timesheet timesheet = timesheetRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TIMESHEET_NOT_FOUND));
        
        if (!"PENDING".equals(timesheet.getStatus())) {
            throw new AppException(ErrorCode.TIMESHEET_ALREADY_PROCESSED);
        }

        timesheetMapper.updateFromDto(timesheetDTO, timesheet);
        return timesheetMapper.toDTO(timesheetRepository.save(timesheet));
    }

    @Override
    public TimesheetDTO getTimesheet(UUID id) {
        return timesheetMapper.toDTO(timesheetRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TIMESHEET_NOT_FOUND)));
    }

    @Override
    public Page<TimesheetDTO> getAllTimesheets(int page, int size) {
        return timesheetRepository.findAll(PageRequest.of(page, size))
                .map(timesheetMapper::toDTO);
    }

    @Override
    @Transactional
    public TimesheetDTO approveTimesheet(UUID id, UUID approverId, String comments) {
        Timesheet timesheet = timesheetRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TIMESHEET_NOT_FOUND));
        
        if (!"PENDING".equals(timesheet.getStatus())) {
            throw new AppException(ErrorCode.TIMESHEET_ALREADY_PROCESSED);
        }
        
        Employee approver = employeeRepository.findById(approverId)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        timesheet.setApprover(approver);
        timesheet.setStatus("APPROVED");
        timesheet.setComments(comments);
        timesheet.setApprovalDate(new Date());

        return timesheetMapper.toDTO(timesheetRepository.save(timesheet));
    }

    @Override
    @Transactional
    public TimesheetDTO rejectTimesheet(UUID id, UUID approverId, String comments) {
        Timesheet timesheet = timesheetRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TIMESHEET_NOT_FOUND));
        
        Employee approver = employeeRepository.findById(approverId)
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));

        timesheet.setApprover(approver);
        timesheet.setStatus("REJECTED");
        timesheet.setComments(comments);
        timesheet.setApprovalDate(new Date());

        return timesheetMapper.toDTO(timesheetRepository.save(timesheet));
    }

    @Override
    public List<TimesheetDTO> getEmployeeTimesheets(UUID employeeId) {
        return timesheetRepository.findByEmployeeId(employeeId).stream()
                .map(timesheetMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTimesheet(UUID id) {
        Timesheet timesheet = timesheetRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TIMESHEET_NOT_FOUND));
        
        if (!"PENDING".equals(timesheet.getStatus())) {
            throw new AppException(ErrorCode.TIMESHEET_ALREADY_PROCESSED);
        }
        
        timesheetRepository.delete(timesheet);
    }
}
