package carevn.luv2code.cms.tevc_cms_api.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import carevn.luv2code.cms.tevc_cms_api.dto.LeaveDTO;
import carevn.luv2code.cms.tevc_cms_api.entity.Employee;
import carevn.luv2code.cms.tevc_cms_api.entity.Leave;
import carevn.luv2code.cms.tevc_cms_api.enums.LeaveStatus;
import carevn.luv2code.cms.tevc_cms_api.exception.AppException;
import carevn.luv2code.cms.tevc_cms_api.exception.ErrorCode;
import carevn.luv2code.cms.tevc_cms_api.mapper.LeaveMapper;
import carevn.luv2code.cms.tevc_cms_api.repository.EmployeeRepository;
import carevn.luv2code.cms.tevc_cms_api.repository.LeaveRepository;
import carevn.luv2code.cms.tevc_cms_api.service.LeaveService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {
    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveMapper leaveMapper;

    @Override
    @Transactional
    public LeaveDTO createLeave(LeaveDTO leaveDTO) {
        Leave leave = leaveMapper.toEntity(leaveDTO);

        Employee employee = employeeRepository
                .findById(leaveDTO.getEmployeeId())
                .orElseThrow(() -> new AppException(ErrorCode.EMPLOYEE_NOT_FOUND));
        leave.setEmployee(employee);
        leave.setStatus(LeaveStatus.PENDING);

        return leaveMapper.toDTO(leaveRepository.save(leave));
    }

    @Override
    @Transactional
    public LeaveDTO updateLeave(Integer id, LeaveDTO leaveDTO) {
        Leave leave = leaveRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LEAVE_NOT_FOUND));

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new AppException(ErrorCode.LEAVE_ALREADY_PROCESSED);
        }

        leaveMapper.updateFromDto(leaveDTO, leave);
        return leaveMapper.toDTO(leaveRepository.save(leave));
    }

    @Override
    public void deleteLeave(Integer id) {
        Leave leave = leaveRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LEAVE_NOT_FOUND));

        if (leave.getStatus() != LeaveStatus.PENDING) {
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

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new AppException(ErrorCode.LEAVE_ALREADY_PROCESSED);
        }

        leave.setStatus(LeaveStatus.APPROVED);
        leave.setApproverComments(comments);
        return leaveMapper.toDTO(leaveRepository.save(leave));
    }

    @Override
    @Transactional
    public LeaveDTO rejectLeave(Integer id, String comments) {
        Leave leave = leaveRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LEAVE_NOT_FOUND));

        if (leave.getStatus() != LeaveStatus.PENDING) {
            throw new AppException(ErrorCode.LEAVE_ALREADY_PROCESSED);
        }

        leave.setStatus(LeaveStatus.REJECTED);
        leave.setApproverComments(comments);
        return leaveMapper.toDTO(leaveRepository.save(leave));
    }

    @Override
    public List<LeaveDTO> getEmployeeLeaves(Integer employeeId) {
        return List.of();
    }

    // ... other standard CRUD methods implementation ...
}
