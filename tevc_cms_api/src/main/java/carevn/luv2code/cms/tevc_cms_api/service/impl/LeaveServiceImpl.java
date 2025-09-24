package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import carevn.luv2code.cms.tevc_cms_api.dto.LeaveDTO;
import carevn.luv2code.cms.tevc_cms_api.dto.requests.TemplateMailRequest;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.entity.Leave;
import carevn.luv2code.cms.tevc_cms_api.enums.LeaveStatus;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.LeaveMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.LeaveRepository;
import carevn.luv2code.cms.tevc_cms_api.service.LeaveService;
import carevn.luv2code.cms.tevc_cms_api.service.MailService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {
    private final MailService mailService;
    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveMapper leaveMapper;

    public LeaveDTO createLeave(LeaveDTO leaveDTO) {
        try {
            Leave leave = leaveMapper.toEntity(leaveDTO);

            Employee employee = employeeRepository
                    .findById(leaveDTO.getEmployeeId())
                    .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
            leave.setEmployee(employee);
            leave.setLeaveStatus(LeaveStatus.PENDING);

            Leave savedLeave = leaveRepository.save(leave);

            List<String> managerEmails = List.of();
            if (employee.getDepartment() != null && employee.getDepartment().getManager() != null) {
                String managerMail = employee.getDepartment().getManager().getEmail();
                if (managerMail != null && !managerMail.isBlank()) {
                    managerEmails = List.of(managerMail);
                }
            }

            TemplateMailRequest mail = getMailRequest(savedLeave, managerEmails);

            mailService.sendTemplate(mail);

            return leaveMapper.toDTO(savedLeave);
        } catch (AppException ex) {
            throw ex;
        } catch (Exception ex) {
            Logger.getLogger(LeaveServiceImpl.class.getName()).severe(ex.getMessage());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @NotNull
    private static TemplateMailRequest getMailRequest(Leave savedLeave, List<String> toEmails) {
        TemplateMailRequest mail = new TemplateMailRequest();
        String employeeName = savedLeave.getEmployee().getFirstName() + " "
                + savedLeave.getEmployee().getLastName();
        mail.setTo(toEmails.isEmpty() ? List.of("hr@example.com") : toEmails);
        mail.setSubject("Đơn xin phép mới từ " + employeeName);
        mail.setTemplateName("leave-request-email");
        mail.setVariables(Map.of(
                "employeeName", employeeName,
                "startDate", savedLeave.getStartDate(),
                "endDate", savedLeave.getEndDate(),
                "reason", savedLeave.getReason(),
                "status", savedLeave.getLeaveStatus()));
        return mail;
    }

    @Override
    @Transactional
    public LeaveDTO updateLeave(Integer id, LeaveDTO leaveDTO) {
        Leave leave = leaveRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LEAVE_NOT_FOUND));

        if (leave.getLeaveStatus() != LeaveStatus.PENDING) {
            throw new AppException(ErrorCode.LEAVE_ALREADY_PROCESSED);
        }

        leaveMapper.updateFromDto(leaveDTO, leave);
        return leaveMapper.toDTO(leaveRepository.save(leave));
    }

    @Override
    public void deleteLeave(Integer id) {
        Leave leave = leaveRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LEAVE_NOT_FOUND));

        if (leave.getLeaveStatus() != LeaveStatus.PENDING) {
            throw new AppException(ErrorCode.LEAVE_ALREADY_PROCESSED);
        }

        leaveRepository.delete(leave);
    }

    @Override
    public LeaveDTO getLeave(Integer id) {
        return null;
    }

    @Override
    public Page<LeaveDTO> getAllLeaves(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return leaveRepository.findAll(pageRequest).map(leaveMapper::toDTO);
    }

    @Override
    @Transactional
    public LeaveDTO approveLeave(Integer id, String comments) {
        Leave leave = leaveRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LEAVE_NOT_FOUND));

        if (leave.getLeaveStatus() != LeaveStatus.PENDING) {
            throw new AppException(ErrorCode.LEAVE_ALREADY_PROCESSED);
        }

        leave.setLeaveStatus(LeaveStatus.APPROVED);
        leave.setApproverComments(comments);
        return leaveMapper.toDTO(leaveRepository.save(leave));
    }

    @Override
    @Transactional
    public LeaveDTO rejectLeave(Integer id, String comments) {
        Leave leave = leaveRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LEAVE_NOT_FOUND));

        if (leave.getLeaveStatus() != LeaveStatus.PENDING) {
            throw new AppException(ErrorCode.LEAVE_ALREADY_PROCESSED);
        }

        leave.setLeaveStatus(LeaveStatus.REJECTED);
        leave.setApproverComments(comments);
        return leaveMapper.toDTO(leaveRepository.save(leave));
    }

    @Override
    public List<LeaveDTO> getEmployeeLeaves(Integer employeeId) {
        return List.of();
    }

    @Override
    public Page<LeaveDTO> getLeavesByEmployeeIdPaged(Integer employeeId, Pageable pageable) {
        return leaveRepository.findByEmployeeId(employeeId, pageable).map(leaveMapper::toDTO);
    }

    //    private void sendMailToManagers(Employee employee, Leave leave) {
    //        if (employee.getDepartment() == null) return;
    //
    //        // Lấy manager(s) của department
    //        Department dept = employee.getDepartment();
    //        if (dept.getManager() == null) return;
    //
    //        // ⚡ Nếu 1 department có nhiều manager thì sửa Department.java:
    //        // @ManyToMany hoặc @OneToMany(mappedBy = "manager")
    //        // giả sử hiện tại chỉ có 1 manager thì handle luôn:
    //        Employee manager = dept.getManager();
    //        if (manager.getEmail() == null) return;
    //
    //        String subject = "[HRM] Nhân viên " + employee.getFirstName() + " " + employee.getLastName()
    //                + " đã tạo đơn nghỉ phép";
    //        String body = String.format(
    //                "Xin chào %s,<br/><br/>" +
    //                        "Nhân viên <b>%s %s</b> đã tạo đơn nghỉ phép.<br/>" +
    //                        "Trạng thái hiện tại: %s<br/><br/>" +
    //                        "Vui lòng truy cập hệ thống để phê duyệt.",
    //                manager.getFirstName(),
    //                employee.getFirstName(),
    //                employee.getLastName(),
    //                leave.getStatus().name()
    //        );
    //
    //        mailService.sendMail(List.of(manager.getEmail()), subject, body, true);
    //    }
}
